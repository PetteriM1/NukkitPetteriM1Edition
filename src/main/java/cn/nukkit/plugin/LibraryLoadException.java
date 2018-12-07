package cn.nukkit.plugin;

public class LibraryLoadException extends RuntimeException {

    public LibraryLoadException(Library library) {
        super("Load library " + library.getArtifactId() + " failed!");
    }
}
