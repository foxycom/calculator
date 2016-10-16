package com.guliash.calculator.state;

import com.guliash.parser.AngleUnits;

public interface AppSettings {
    AngleUnits getAngleUnits();

    void saveAngleUnits(AngleUnits angleUnits);

    boolean isReviewInviteShown();

    void shownReviewInvite();
}
