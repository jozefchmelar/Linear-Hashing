package test
import LinearHashing.LinearHashingFile
import io.kotlintest.specs.StringSpec
import model.Patient
import model.PatientId
import model.instanceOfPatientRecord
import model.toRecord
import java.util.*

class BlockAndRecordSerialization : StringSpec({

    val ds = LinearHashingFile(
        pathToFile = "test_patients",
        instanceOfType = instanceOfPatientRecord,
        numberOfRecordsInAdditionalBlock = 2,
        maxDensity = 0.75,
        minDensity = 0.55,
        numberOfRecordsInBlock = 3,
        blockCount = 2,
        deleteFiles = true
    )

    val patients = (1..5000).map { Patient(PatientId(it)).toRecord() }

    "insert test"{
        patients.forEach {
            val success = ds.add(it)
            if(!success){
                println("boha. $it")
            }

        }
    }

})