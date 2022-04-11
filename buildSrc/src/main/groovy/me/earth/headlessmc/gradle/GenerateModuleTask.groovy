package me.earth.headlessmc.gradle

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class GenerateModuleTask extends DefaultTask {
    @Input
    abstract Property<ModuleExtension> getExtension()

    @TaskAction
    void generateModule() {
        def me = extension.get()
        if (me.getName().getOrNull() == null) {
            project.logger.error("ModulePlugin will not generate a" +
                    " module-info.class, no name has been specified.")
            return
        }

        def file = me.directory.get().file('module-info.class').asFile
        file.parentFile.mkdirs()
        try (def fos = new FileOutputStream(file)) {
            def cw = Extension2ClassWriterAdapter.toClassWriter(me)
            fos.write(cw.toByteArray())
        }
    }

}
