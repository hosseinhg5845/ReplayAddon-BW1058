package dev.sosis.replay.configs.language;

import dev.sosis.replay.configs.Messages;

import java.util.List;

public interface ILang {
    public String getCurrent(Messages m, boolean colorize);
    public List<String> getCurrentList(Messages m);
    public void createFile();
}
