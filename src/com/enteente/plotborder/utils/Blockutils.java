package com.enteente.plotborder.utils;

import com.github.intellectualsites.plotsquared.plot.object.BlockBucket;
import com.github.intellectualsites.plotsquared.plot.object.PlotBlock;
import com.github.intellectualsites.plotsquared.plot.util.StringComparison;
import com.github.intellectualsites.plotsquared.plot.util.WorldUtil;


public class Blockutils {
	
	
	public static final boolean preventUnsafe=false;
	
	public static final BlockBucket parseString(final String string) {
        
		if (string == null || string.isEmpty()) {
            return new BlockBucket();
        }
        final BlockBucket blockBucket = new BlockBucket();
        final String[] parts = string.split(",");
        
        
        
        for (final String part : parts) {
            String block;
            int chance = -1;

            if (part.contains(":")) {
                final String[] innerParts = part.split(":");
                if (innerParts.length > 1) {
                    chance = Integer.parseInt(innerParts[1]);
                }
                block = innerParts[0];
            } else {
                block = part;
            }
            final StringComparison<PlotBlock>.ComparisonResult value =
                WorldUtil.IMP.getClosestBlock(block);
            if (value == null) {
                throw new UnknownBlockException(block);
            } else if (Blockutils.preventUnsafe  && !value.best.isAir()
                && !WorldUtil.IMP.isBlockSolid(value.best)) {
                throw new UnsafeBlockException(value.best);
            }
            blockBucket.addBlock(value.best, chance);
        }
        blockBucket.compile(); // Pre-compile :D
        return blockBucket;
    }
	
	
    public static final class UnknownBlockException extends IllegalArgumentException {

        private final String unknownValue;
        
        public final String getUnknownValue() {
        	return this.unknownValue;
        }

        UnknownBlockException(final String unknownValue) {
            super(String.format("\"%s\" is not a valid block", unknownValue));
            this.unknownValue = unknownValue;
        }

    }
    
    
    public static final class UnsafeBlockException extends IllegalArgumentException {

        private final PlotBlock unsafeBlock;

        public final PlotBlock getnsafeBlock() {
        	return this.unsafeBlock;
        }

        UnsafeBlockException(final PlotBlock unsafeBlock) {
            super(String.format("%s is not a valid block", unsafeBlock));
            this.unsafeBlock = unsafeBlock;
        }

    }
    
}

