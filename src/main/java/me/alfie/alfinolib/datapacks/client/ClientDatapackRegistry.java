package me.alfie.alfinolib.datapacks.client;

import me.alfie.alfinolib.datapacks.DataMap;
import me.alfie.alfinolib.datapacks.DatapackKey;
import me.alfie.alfinolib.datapacks.ModDatapack;

import java.util.HashMap;
import java.util.Map;

/**
 * Client-sided datapack registration. The client cannot store a map of DatapackKeys and raw Datapack objects.
 * Instead, the client has access to the common DatapackKey and the processed DataMap
 */
public final class ClientDatapackRegistry {

    private static final Map<DatapackKey<?>, DataMap> DATAMAPS = new HashMap<>();

    public static void register() {

    }
}
