package AbstractData

import record.*
import record.SizeConst.*
import record.Validity.Valid
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.util.*

data class Person(var name:String, var lastName:String, var birthDate: Date = Date() ) : Record<Person> {

    override val stringSize: Int
        get() = 50

    override fun toByteArray() = toBytes {
        writeValidity(validity)
        writeString(name)
        writeString(lastName)
        writeDate(birthDate)
    }

    override fun fromByteArray(byteArray: ByteArray): Person {
        val dis  = DataInputStream(ByteArrayInputStream(byteArray))
        val validity = dis.readValidity()
        val name = dis.readString()
        val last = dis.readString()
        val date = dis.readDate()
        return Person(name, last, date).apply {
            this.validity = validity
        }

    }

    override val byteSize: Int
        get() = 2* stringByteSize() + SizeOfDate +  SizeOfValidity

    override var validity: Validity = Valid



}


