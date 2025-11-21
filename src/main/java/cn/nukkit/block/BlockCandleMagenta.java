package cn.nukkit.block;

public class BlockCandleMagenta extends BlockCandle {

    public BlockCandleMagenta() {
        this(0);
    }

    public BlockCandleMagenta(int meta) {
        super(meta);
    }

    @Override
    public int getId() {
        return MAGENTA_CANDLE;
    }

    @Override
    public String getName() {
        return "Magenta Candle";
    }
}
