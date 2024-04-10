package rs.ac.metropolitan.cs330_dz07

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import rs.ac.metropolitan.cs330_dz07.ui.theme.CS330DZ07Theme


class MainActivity : ComponentActivity() {
    var SENT = "SMS_SENT"
    var DELIVERED = "SMS_DELIVERED"
    var sentPI: PendingIntent? = null
    var deliveredPI:PendingIntent? = null
    var smsSentReceiver: BroadcastReceiver? = null
    var smsDeliveredReceiver:BroadcastReceiver? = null
    var intentFilter: IntentFilter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sentPI = PendingIntent.getBroadcast(
            this, 0,
            Intent(SENT), PendingIntent.FLAG_IMMUTABLE
        )
        deliveredPI = PendingIntent.getBroadcast(
            this, 0,
            Intent(DELIVERED), PendingIntent.FLAG_IMMUTABLE
        )
        setContent {
            CS330DZ07Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyApp()
                }
            }
        }
    }


@Composable
fun MyCustomUI() {
    val context = LocalContext.current
    val smsContent = remember { mutableStateOf("") }
    val emailContent = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = smsContent.value,
            onValueChange = { smsContent.value = it },
            label = { Text("Content") },
            modifier = Modifier.padding(8.dp)
        )

        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            Button(onClick = { sendSMS("5556", smsContent.value) }) {
                Image(
                    painter = painterResource(id = R.drawable.sms_icon),
                    contentDescription = "SMS Icon",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Button(onClick = { val to = arrayOf(
                "ime.prezime@metropolitan.ac.rs",
                "student1@metropolitan.ac.rs"
            )
                val cc = arrayOf("student2@metropolitan.ac.rs")
                sendEmail(to, cc, "Pozdrav!!!", smsContent.value) }) {
                Image(
                    painter = painterResource(id = R.drawable.email_icon),
                    contentDescription = "Email Icon",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }




    }
}

@Composable
fun MyApp() {
    Surface(color = Color.White) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            MyCustomUI()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CS330DZ07Theme {
        Greeting("Android")
    }
}

private fun sendSMS(
    phoneNumber: String,
    message: String
) {
    val smsManager = this.getSystemService(SmsManager::class.java)
    smsManager.sendTextMessage(
        phoneNumber, null, message, sentPI, deliveredPI
    )}

    fun onSMSIntentClick(v: View?) {
        val i = Intent(Intent.ACTION_VIEW)
        i.putExtra("address", "5556; 5558; 5560")
        i.putExtra("sms_body", "Pozdravni SMS - primer!")
        i.type = "vnd.android-dir/mms-sms"
        startActivity(i)
    }

private fun sendEmail(
    emailAddresses: Array<String>, carbonCopies: Array<String>,
    subject: String, message: String
) {
    val emailIntent = Intent(Intent.ACTION_SEND)
    emailIntent.data = Uri.parse("mailto:")
    emailIntent.putExtra(Intent.EXTRA_EMAIL, emailAddresses)
    emailIntent.putExtra(Intent.EXTRA_CC, carbonCopies)
    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
    emailIntent.putExtra(Intent.EXTRA_TEXT, message)
    emailIntent.type = "message/rfc822"
    startActivity(Intent.createChooser(emailIntent, "Email"))
}
}