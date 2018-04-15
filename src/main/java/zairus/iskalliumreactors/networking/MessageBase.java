package zairus.iskalliumreactors.networking;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.io.*;

public class MessageBase implements IMessage {
    private ByteArrayOutputStream arrayout;
    private DataOutputStream dataout;
    public DataInputStream datain;

    public MessageBase() {
        arrayout = new ByteArrayOutputStream();
        dataout = new DataOutputStream(arrayout);
    }

    public MessageBase(byte[] data) {
        datain = new DataInputStream(new ByteArrayInputStream(data));
    }

    public MessageBase addInt(int data) {
        try {
            dataout.writeInt(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public int getInt() {
        try {
            return datain.readInt();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public MessageBase addDouble(double data) {
        try {
            dataout.writeDouble(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public double getDouble() {
        try {
            return datain.readDouble();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public MessageBase addString(String data) {
        try {
            dataout.writeUTF(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String getString() {
        try {
            return datain.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public MessageBase addBoolean(boolean data) {
        try {
            dataout.writeBoolean(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    public boolean getBoolean() {
        try {
            return datain.readBoolean();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        byte[] bytes = new byte[buf.capacity()];
        buf.getBytes(0, bytes);
        datain = new DataInputStream(new ByteArrayInputStream(bytes));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBytes(arrayout.toByteArray());
    }
}
