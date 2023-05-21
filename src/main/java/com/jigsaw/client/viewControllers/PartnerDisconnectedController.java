package com.jigsaw.client.viewControllers;

import static com.jigsaw.client.SharedComponents.viewManager;

public class PartnerDisconnectedController {

    public void onWaitForPartnerButtonClick() {
        viewManager.awaitPlayers();
    }

    public void onQuitButtonClick() {
        viewManager.closeApp();
    }
}
