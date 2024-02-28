package com.hyunrian.project.validation;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

import static com.hyunrian.project.validation.ValidationSequence.*;


@GroupSequence({Default.class, FirstCheckGroup.class, SecondCheckGroup.class})
public interface ValidationSequence {
    interface FirstCheckGroup {};
    interface SecondCheckGroup {};
}
