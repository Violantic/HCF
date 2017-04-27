package me.finestdev.components.utils;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Cooldown {

	private Map<UUID, CooldownBase> cooldowns;
	private final File saveTo;
	private Consumer<UUID> onEndSequence = null;

	public Cooldown(File saveTo) {
		cooldowns = new ConcurrentHashMap<>();

		if (!saveTo.getParentFile().exists())
			saveTo.getParentFile().mkdirs();

		this.saveTo = saveTo;
		try {
			if (!saveTo.exists())
				saveTo.createNewFile();
			BufferedReader reader = new BufferedReader(new FileReader(saveTo));
			String line = null;
			while ((line = reader.readLine()) != null) {
				try {
					String[] vals = line.split(";");
					UUID id = UUID.fromString(vals[0]);
					cooldowns.putIfAbsent(id, new CooldownBase(Long.parseLong(vals[1]), Long.parseLong(vals[2])));
				} catch (Exception ex) {
					continue;
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		fixCounts();
	}

	public CooldownBase get(UUID id) {
		return cooldowns.get(id);
	}

	public void startRunning(JavaPlugin plugin) {
		new BukkitRunnable() {
			@Override
			public void run() {
				fixCounts();
			}
		}.runTaskTimer(plugin, 20, 20);
		new BukkitRunnable() {
			@Override
			public void run() {
				try {
					save();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.runTaskTimerAsynchronously(plugin, 60, 60);
	}

	public void save() throws IOException {
		if (!saveTo.exists())
			saveTo.createNewFile();
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					Map<UUID, CooldownBase> map = Collections.unmodifiableMap(cooldowns);
					PrintWriter writer = new PrintWriter(saveTo);
					for (Entry<UUID, CooldownBase> entry : map.entrySet()) {
						writer.println(entry.getKey().toString() + ";" + String.valueOf(entry.getValue().getStart())
								+ ";" + String.valueOf(entry.getValue().getTime()));
					}
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void startCooldown(UUID id, long timeForCooldown) {
		if (cooldowns.containsKey(id))
			cooldowns.remove(id);
		cooldowns.put(id, new CooldownBase(System.currentTimeMillis(), timeForCooldown));
	}

	public void endCooldown(UUID id) {
		cooldowns.remove(id);
	}

	public Map<UUID, CooldownBase> getCooldowns() {
		return Collections.unmodifiableMap(cooldowns);
	}

	public void fixCounts() {
		cooldowns.forEach((id, base) -> {
            if (getAmountLeft(base) <= 0)
                EventQueue.invokeLater(() -> {
                    cooldowns.remove(id);
                    if (onEndSequence != null)
                        onEndSequence.accept(id);
                });
        });
	}

	public void setOnEndSequece(Consumer<UUID> onEndSequence) {
		this.onEndSequence = onEndSequence;
	}

	public static long getAmountLeft(CooldownBase base) {
		long current = System.currentTimeMillis();
		long start = base.start;
		if (start > current)
			return 0;
		long remaining = base.time - (current - start);
		if (remaining <= 0)
			return 0;
		return remaining;
	}

	public static long timeToMillis(String time) {
		long full = 0;
		String[] timeSplit = time.split(":");
		for (String base : timeSplit) {
			Container<Time, Long> got = Time.match(base);
			if (got != null)
				full += got.getFirst().get(got.getSecond());
		}
		return full;
	}

	public static Map<Time, Long> timeFromMillis(long millis) {
		long current = millis;
		Map<Time, Long> times = new HashMap<>();
		for (Time time : Time.values()) {
			Container<Long, Long> received = time.getRemaining(current);
			current = received.getSecond();
			long got = received.getFirst();
			if (got > 0)
				times.put(time, got);
		}
		return times;
	}

	public class CooldownBase {

		private final long start;
		private final long time;

		public CooldownBase(long start, long time) {
			this.start = start;
			this.time = time;
		}

		public long getStart() {
			return start;
		}

		public long getTime() {
			return time;
		}

	}

	public static enum Time {

		WEEK((((1000 * 60) * 60) * 24) * 7), DAY(((1000 * 60) * 60) * 24), HOUR((1000 * 60) * 60), MINUTE(
				1000 * 60), SECOND(1000), MILLISECOND(1);

		public long milliPer;

		private Time(long milliPer) {
			this.milliPer = milliPer;
		}

		public long get(long amount) {
			return milliPer * amount;
		}

		public Container<Long, Long> getRemaining(long amount) {
			if (this == Time.MILLISECOND)
				return Container.of(amount, 0L);
			return Container.of((long) amount / milliPer, (long) amount % milliPer);
		}

		public static Container<Time, Long> match(String string) {
			String get = string.replaceAll("[0-9\\.]", "").toLowerCase();
			long amount = 0;
			String numb = string.replaceAll("[^0-9\\.]", "").split("\\.")[0];
			if (numb.isEmpty())
				return null;
			amount = Integer.parseInt(numb);
			if (get.startsWith("w"))
				return Container.of(Time.WEEK, amount);
			else if (get.startsWith("d"))
				return Container.of(Time.DAY, amount);
			else if (get.startsWith("h"))
				return Container.of(Time.HOUR, amount);
			else if (get.startsWith("mil"))
				return Container.of(Time.MILLISECOND, amount);
			else if (get.startsWith("m"))
				return Container.of(Time.MINUTE, amount);
			else
				return Container.of(Time.SECOND, amount);
		}

	}

	public static class Container<K, V> {

		private final K first;
		private final V second;

		private Container() {
			this(null, null);
		}

		private Container(K first, V second) {
			this.first = first;
			this.second = second;
		}

		public K getFirst() {
			return first;
		}

		public V getSecond() {
			return second;
		}

		@Override
		public String toString() {
			return "{first=\"" + String.valueOf(first) + "\",second=\"" + String.valueOf(second) + "\"}";
		}

		public static <K, V> Container<K, V> of(K first, V second) {
			return new Container<K, V>(first, second);
		}

	}

}
