package de.fhkiel.pepper.cms.modules;

import java.io.File;

import dalvik.system.DexClassLoader;

public class ModuleLoader {

    public Object loadModule(String libPath, File tmpDir, String classToLoad) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        DexClassLoader classLoader = new DexClassLoader(libPath, tmpDir.getAbsolutePath(), null, this.getClass().getClassLoader());
        Class<Object> classObject = (Class<Object>) classLoader.loadClass(classToLoad);
        return classObject.newInstance();
    }

}
