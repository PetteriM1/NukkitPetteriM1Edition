package cn.nukkit.block;

public class BlockCandleGray extends BlockCandle {

    public BlockCandleGray() {
        this(0);
    }

    public BlockCandleGray(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return GRAY_CANDLE;
    }

    @Override
    public String getName() {
        return "Gray Candle";
    }
}
