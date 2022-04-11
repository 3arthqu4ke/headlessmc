//file:noinspection unused
package me.earth.headlessmc.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class ModulePlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        def me = project.extensions.create('module', ModuleExtension)
        me.directory.set(project.layout.buildDirectory.dir('resources/main'))
        def task = project.tasks.register('genModuleInfo', GenerateModuleTask, {
            extension.set(me)
        })

        project.tasks.getByName('jar', jar -> jar.dependsOn(task))
    }

}
