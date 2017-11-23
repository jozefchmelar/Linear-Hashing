package LinearHashing
import AbstractData.*
import AbstractData.SizeConst.*
import record.*
import java.io.ByteArrayInputStream
import java.io.DataInputStream

class LinearHashFileBlock<T: Record<T>> : Block<T> {

    override var addressInFile: Int
    override val blockSize: Int
    override var recordCount: Int
        get() = data.size
    override var data : MutableList<T>
    override val ofType : T
    override fun toString() = data.toString()

    constructor(blockSize:Int, ofType : T,addressInFile :Int = 0) {
        this.blockSize     = blockSize
        this.recordCount   = 0
        this.addressInFile = addressInFile
        this.data          = emptyMutableList()
        this.ofType        = ofType
    }

    constructor(blockSize: Int,ofType: T, data : MutableList<T>){
        this.blockSize      = blockSize
        this.recordCount    = data.size
        this.data           = data
        this.addressInFile  = 0
        this.ofType         = ofType
    }

    override fun toByteArray() = toBytes {
        writeValidity(validity)
        writeInt(addressInFile)
        writeInt(blockSize)
        writeInt(recordCount)
        for(i in 0 until blockSize){
            val record = data.getOrNull(i)
            if(record!=null)
                write(record.toByteArray())
            else
                write(ByteArray(ofType.byteSize))
        }
    }


    override fun fromByteArray(byteArray: ByteArray): Block<T> {
        val b = DataInputStream(ByteArrayInputStream(byteArray))

        val valid = b.readValidity()
        val addressInFile = b.readInt()
        val blockSize = b.readInt()
        val recordCount = b.readInt()
        val recordSize = ofType.byteSize
        val readList = emptyMutableList<T>()

        for (i in 0 until recordCount) {
            val bytes = ByteArray(recordSize)
            for (j in 0 until recordSize)
                bytes[j] = b.readByte()

            readList.add(ofType.fromByteArray(bytes))
        }

        return LinearHashFileBlock(blockSize, ofType).apply {
            this.validity=valid
            this.addressInFile=addressInFile
            this.recordCount=recordCount
            this.data = readList
        }

    }

    override val byteSize: Int
        get() = SizeOfValidity + SizeOfInt + SizeOfInt + SizeOfInt + (blockSize * ofType.byteSize)
    override var validity: Validity = Validity.Valid

}