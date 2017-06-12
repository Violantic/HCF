package me.borawski.hcf.manuel;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 5/16/2017.
 */
public class ManualPage {

    private String title;
    private List<String> contents;
    private List<ManualButton> buttons;

    public ManualPage(String title) {
        this.title = title;
        this.contents = new ArrayList<>();
        this.buttons = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public List<ManualButton> getButtons() {
        return buttons;
    }

    public void setButtons(List<ManualButton> buttons) {
        this.buttons = buttons;
    }

    public ManualPage addString(String s) {
        getContents().add(s.replace("&", ChatColor.COLOR_CHAR + ""));
        return this;
    }

    public ManualPage addButton(ManualButton button) {
        getContents().add("\n" + ChatColor.DARK_GRAY + "§lClick:§r " + button.getColor() + button.getName());
        getButtons().add(button);
        return this;
    }

}
