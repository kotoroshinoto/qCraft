/*
Copyright 2014 Google Inc. All rights reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/


package dan200.qcraft.shared;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EnumFaceDirection;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class QuantumUtil
{
    private static Block getBlock( IBlockAccess world, int x, int y, int z )
    {
        return world.getBlock( x, y, z );
    }

    public static boolean getRedstoneSignal( World world, int x, int y, int z, int side )
    {
        Block block = getBlock( world, x, y, z );
        BlockPos pos = new BlockPos(x, y, z);
        if( block != null )
        {
            if( block == Blocks.redstone_wire )
            {
                int metadata = world.getBlockMetadata( x, y, z );
                return ( side != 1 && metadata > 0 ) ? true : false;
            }
            else if( block.canProvidePower() )
            {
                EnumFacing testSide = EnumFacing.values()[side];//oppositeSide[ side ];
                int power = block.isProvidingWeakPower(world, pos, block.getDefaultState(), testSide);
                return ( power > 0 ) ? true : false;
            } else if (world.func_175677_d(pos, false))
            {
                for( int i = 0; i < 6; ++i )
                {
                    if( i != side )
                    {
                        int testX = x + Facing.offsetsXForSide[ i ];
                        int testY = y + Facing.offsetsYForSide[ i ];
                        int testZ = z + Facing.offsetsZForSide[ i ];
                        BlockPos pos1 = new BlockPos(testX, testY, testZ);
                        Block neighbour = getBlock( world, testX, testY, testZ );

                        if( neighbour != null && neighbour.canProvidePower() )
                        {
                            //TODO:Review
                            int power = neighbour.isProvidingStrongPower(world, pos1, block.getDefaultState(), EnumFacing.getFront(i));
                            if( power > 0 )
                            {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        }
        return false;
    }
}
