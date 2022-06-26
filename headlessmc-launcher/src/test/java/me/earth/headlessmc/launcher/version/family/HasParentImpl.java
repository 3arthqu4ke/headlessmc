package me.earth.headlessmc.launcher.version.family;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.earth.headlessmc.api.HasName;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HasParentImpl implements HasParent<HasParentImpl>, HasName {
    private final String name = "Only because required by the FamilyCleaner.";
    private HasParentImpl parent;

}
