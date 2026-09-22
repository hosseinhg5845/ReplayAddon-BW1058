package dev.sosis.replay.spigui.pagination;


import dev.sosis.replay.spigui.SGMenu;
import dev.sosis.replay.spigui.buttons.SGButton;

public interface SGPaginationButtonBuilder {

    SGButton buildPaginationButton(SGPaginationButtonType type, SGMenu inventory);

}
