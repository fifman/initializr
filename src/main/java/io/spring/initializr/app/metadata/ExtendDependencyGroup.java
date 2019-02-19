package io.spring.initializr.app.metadata;

import io.spring.initializr.metadata.Dependency;
import io.spring.initializr.metadata.DependencyGroup;

import java.util.ArrayList;
import java.util.List;

public class ExtendDependencyGroup extends DependencyGroup {

    public void setContent(List<Dependency> content) {
        this.getContent().addAll(content);
    }
}
