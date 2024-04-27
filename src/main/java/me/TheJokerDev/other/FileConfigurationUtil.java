package me.TheJokerDev.other;

import java.nio.file.*;
import org.bukkit.configuration.file.*;
import java.io.*;
import me.TheJokerDev.futureholograms.utils.*;
import org.bukkit.configuration.*;
import java.util.*;

public class FileConfigurationUtil
{
    private FileConfiguration config;
    private final File file;
    
    public FileConfigurationUtil(final File file) {
        this.file = file;
        this.load();
    }
    
    public FileConfigurationUtil(final File folder, final String fileName) {
        this(new File(folder(folder), fileName));
    }
    
    public static File folder(final File folder) {
        if (!folder.exists()) {
            folder.mkdir();
        }
        return folder;
    }
    
    public static boolean isSymlink(final File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        }
        return Files.isSymbolicLink(file.toPath());
    }
    
    public static <T> T requireNonNull(final T obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        return obj;
    }
    
    private void load() {
        try {
            if (!this.file.exists()) {
                this.file.createNewFile();
            }
            this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file);
        }
        catch (Exception var2) {
            var2.printStackTrace();
        }
    }
    
    public void clear() {
        try {
            destroyFile(this.file);
            if (!this.file.exists()) {
                this.file.createNewFile();
            }
            this.reload();
        }
        catch (Exception var2) {
            var2.printStackTrace();
        }
    }
    
    public static void cleanDirectory(final File directory) throws IOException {
        final File[] files = verifiedListFiles(directory);
        IOException exception = null;
        final File[] var3 = files;
        for (int var4 = files.length, var5 = 0; var5 < var4; ++var5) {
            final File file = var3[var5];
            try {
                forceDelete(file);
            }
            catch (IOException var6) {
                exception = var6;
            }
        }
        if (null != exception) {
            throw exception;
        }
    }
    
    private static File[] verifiedListFiles(final File directory) throws IOException {
        if (!directory.exists()) {
            final String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }
        if (!directory.isDirectory()) {
            final String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }
        final File[] files = directory.listFiles();
        if (files == null) {
            throw new IOException("Failed to list contents of " + directory);
        }
        return files;
    }
    
    public static void deleteDirectory(final File directory) throws IOException {
        if (directory.exists()) {
            if (!isSymlink(directory)) {
                cleanDirectory(directory);
            }
            if (!directory.delete()) {
                final String message = "Unable to delete directory " + directory + ".";
                throw new IOException(message);
            }
        }
    }
    
    public static void destroyFile(final File file) throws Exception {
        forceDelete(file);
    }
    
    public static void forceDelete(final File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        }
        else {
            final boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent) {
                    throw new FileNotFoundException("File does not exist: " + file);
                }
                final String message = "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }
    
    public void reload() {
        this.config = (FileConfiguration)YamlConfiguration.loadConfiguration(this.file);
    }
    
    public void save() {
        try {
            this.config.save(this.file);
        }
        catch (Exception var2) {
            var2.printStackTrace();
        }
    }
    
    public void set(final String path, Object value) {
        if (value instanceof Float) {
            final float val = (float)value;
            value = Float.toString(val);
        }
        this.config.set(path, value);
        this.save();
    }
    
    public void add(final String path, final Object value) {
        if (!this.contains(path)) {
            this.set(path, value);
        }
    }
    
    public boolean contains(final String path) {
        return this.config.contains(path);
    }
    
    public Object get(final String path) {
        return this.config.get(path);
    }
    
    public Object get(final String path, final Object def) {
        this.add(path, def);
        return this.get(path);
    }
    
    public String getString(final String path) {
        return this.config.getString(Utils.ct(path));
    }
    
    public String getString(final String path, final String def) {
        this.add(path, def);
        return this.getString(path);
    }
    
    public boolean getBoolean(final String path) {
        return this.config.getBoolean(path);
    }
    
    public boolean getBoolean(final String path, final boolean def) {
        this.add(path, def);
        return this.getBoolean(path);
    }
    
    public int getInt(final String path) {
        return this.config.getInt(path);
    }
    
    public int getInt(final String path, final int def) {
        this.add(path, def);
        return this.getInt(path);
    }
    
    public double getDouble(final String path) {
        return this.config.getDouble(path);
    }
    
    public double getDouble(final String path, final double def) {
        this.add(path, def);
        return this.getDouble(path);
    }
    
    public long getLong(final String path) {
        return this.config.getLong(path);
    }
    
    public long getLong(final String path, final long def) {
        this.add(path, def);
        return this.getLong(path);
    }
    
    public float getFloat(final String path) {
        return (float)Long.parseLong(this.getString(path));
    }
    
    public float getFloat(final String path, final float def) {
        this.add(path, def);
        return this.getFloat(path);
    }
    
    public List<?> getList(final String path) {
        return (List<?>)this.config.getList(path);
    }
    
    public List<?> getList(final String path, final List<?> def) {
        this.add(path, def);
        return this.getList(path);
    }
    
    public List<String> getStringList(final String path) {
        return (List<String>)this.config.getStringList(path);
    }
    
    public List<String> getStringList(final String path, final List<String> def) {
        this.add(path, def);
        return this.getStringList(path);
    }
    
    public List<Boolean> getBooleanList(final String path) {
        return (List<Boolean>)this.config.getBooleanList(path);
    }
    
    public List<Boolean> getBooleanList(final String path, final List<Boolean> def) {
        this.add(path, def);
        return this.getBooleanList(path);
    }
    
    public List<Integer> getIntList(final String path) {
        return (List<Integer>)this.config.getIntegerList(path);
    }
    
    public List<Integer> getIntList(final String path, final List<Integer> def) {
        this.add(path, def);
        return this.getIntList(path);
    }
    
    public List<Double> getDoubleList(final String path) {
        return (List<Double>)this.config.getDoubleList(path);
    }
    
    public List<Double> getDoubleList(final String path, final List<Double> def) {
        this.add(path, def);
        return this.getDoubleList(path);
    }
    
    public List<Long> getLongList(final String path) {
        return (List<Long>)this.config.getLongList(path);
    }
    
    public List<Long> getLongList(final String path, final List<Long> def) {
        this.add(path, def);
        return this.getLongList(path);
    }
    
    public List<Float> getFloatList(final String path) {
        return (List<Float>)this.config.getFloatList(path);
    }
    
    public List<Float> getFloatList(final String path, final List<Float> def) {
        this.add(path, def);
        return this.getFloatList(path);
    }
    
    public ConfigurationSection getSection(final String path) {
        return this.config.getConfigurationSection(path);
    }
    
    public Set<String> getKeys(final boolean deep) {
        return (Set<String>)this.config.getKeys(deep);
    }
    
    public FileConfiguration getConfig() {
        return this.config;
    }
    
    public File getFile() {
        return this.file;
    }
}
