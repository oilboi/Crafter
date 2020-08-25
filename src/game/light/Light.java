package game.light;

import game.ChunkHandling.Chunk;

import java.util.ArrayList;

import static game.ChunkHandling.ChunkData.*;
import static game.ChunkHandling.ChunkMath.genHash;
import static game.Crafter.chunkRenderDistance;

public class Light {

    private static final float debugLightLevel = 15;

    public static void floodFill(int chunkX, int chunkZ) {
        Chunk thisChunk = getChunk(chunkX,chunkZ);

        int    [][][] pseudoChunk     = new     int[16][128][16];
        int    [][][] caveLight       = new     int[16][128][16];

        //shove everything into the 3D arrays
        //fill sun rays with sunlight
        for (int x = 0; x <= 15; x++){
            for(int z = 0; z <= 15; z++){
                for (int y = 0; y <= 127; y++){
                    short thisBlock = getBlockInChunk(x,y,z, chunkX,chunkZ);
                    //lit air
                    if (thisBlock == 0 && underSunlight(x,y,z,chunkX,chunkZ)) {
                        pseudoChunk[x][y][z] = 2;
                        thisChunk.setLight(genHash(x,y,z), (byte)debugLightLevel);
                        //cave air
                    } else if (thisBlock == 0 && !underSunlight(x,y,z,chunkX,chunkZ)){
                        pseudoChunk[x][y][z] = 1;
                        thisChunk.setLight(genHash(x,y,z), (byte)0);
                    } else {
                        pseudoChunk[x][y][z] = -1;
                        thisChunk.setLight(genHash(x,y,z), (byte)0);
                    }
                }
            }
        }

        //2 is air light source
        //1 is cave light
        //-1 is any solid block

        //flood the whole thing with sunlight
        //this is the most ridiculous code I've ever written
        for(int y = 127; y > 0; y--){
            for (int x = 0; x <= 15; x++){
                for(int z = 0; z <= 15; z++) {
                    //only get if cave light
                    if(pseudoChunk[x][y][z] == 1) {
                        //index surroundings
                        for (int xx = -1; xx <= 1; xx++) {
                            for (int yy = -1; yy <= 1; yy++) {
                                for (int zz = -1; zz <= 1; zz++) {
                                    //only do binary direction
                                    if(Math.abs(xx) + Math.abs(yy) + Math.abs(zz) == 1) {
                                        int thisIndexX = x + xx;
                                        int thisIndexY = y + yy;
                                        int thisIndexZ = z + zz;
                                        //catch boundaries
                                        if (thisIndexX >= 0 && thisIndexX <= 15 && thisIndexY >= 0 && thisIndexY <= 127 && thisIndexZ >= 0 && thisIndexZ <= 15) {
                                            //get if sunlight
                                            if (pseudoChunk[thisIndexX][thisIndexY][thisIndexZ] == 2){
                                                //begin distribution queue
                                                ArrayList queue = new ArrayList();
                                                queue.add(new int[]{thisIndexX,thisIndexY,thisIndexZ, (int) debugLightLevel});
                                                while(queue.size() > 0){
                                                    int[] firstIndex = (int[]) queue.get(0);
                                                    int currentLight = firstIndex[3];
                                                    //only pass on light if not pitch black
                                                    if(currentLight > 1) {
                                                        //index surroundings
                                                        for (int xxx = -1; xxx <= 1; xxx++) {
                                                            for (int yyy = -1; yyy <= 1; yyy++) {
                                                                for (int zzz = -1; zzz <= 1; zzz++) {
                                                                    if(Math.abs(xxx) + Math.abs(yyy) + Math.abs(zzz) == 1) {
                                                                        int thisIndexX2 = firstIndex[0] + xxx;
                                                                        int thisIndexY2 = firstIndex[1] + yyy;
                                                                        int thisIndexZ2 = firstIndex[2] + zzz;
                                                                        //catch boundaries
                                                                        if (thisIndexX2 >= 0 && thisIndexX2 <= 15 && thisIndexY2 >= 0 && thisIndexY2 <= 127 && thisIndexZ2 >= 0 && thisIndexZ2 <= 15) {
                                                                            //only spread to cave light that's darker
                                                                            if (pseudoChunk[thisIndexX2][thisIndexY2][thisIndexZ2] == 1 && caveLight[thisIndexX2][thisIndexY2][thisIndexZ2] < currentLight) {
                                                                                caveLight[thisIndexX2][thisIndexY2][thisIndexZ2] = currentLight - 1;
                                                                                queue.add(new int[]{thisIndexX2, thisIndexY2, thisIndexZ2, currentLight - 1});
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    //NEVER REMOVE THIS
                                                    queue.remove(firstIndex);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        for(int y = 127; y > 0; y--) {
            for (int x = 0; x <= 15; x++) {
                for (int z = 0; z <= 15; z++) {
                    if(pseudoChunk[x][y][z] == 1) {
                        int newCaveLight = caveLight[x][y][z];
                        thisChunk.setLight(genHash(x, y, z), (byte) newCaveLight);
                    }
                }
            }
        }
    }
}
