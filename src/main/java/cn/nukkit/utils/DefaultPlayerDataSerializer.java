package cn.nukkit.utils;

import cn.nukkit.Server;
import com.google.common.base.Preconditions;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.util.Optional;
import java.util.UUID;

/**
 * Default player data serializer that saves the player data as .dat files into the 'players' folder.
 */
@RequiredArgsConstructor
public class DefaultPlayerDataSerializer implements PlayerDataSerializer {

    private String dataPath;

    public DefaultPlayerDataSerializer(Server server) {
        this(server.getDataPath());
    }

    public DefaultPlayerDataSerializer(String dataPath) {
        this.dataPath = dataPath;
    }

    @Override
    public Optional<InputStream> read(String name, UUID uuid) throws IOException {
        File file = new File(dataPath  + "players/" + name + ".dat");
        if (!file.exists()) {
            return Optional.empty();
        }
        return Optional.of(new FileInputStream(file));

    }

    @Override
    public OutputStream write(String name, UUID uuid) throws IOException {
        Preconditions.checkNotNull(name, "name");
        File file = new File(dataPath  + "players/" + name + ".dat");
        return new FileOutputStream(file);
    }
}
