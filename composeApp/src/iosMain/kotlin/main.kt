import androidx.compose.ui.window.ComposeUIViewController
import com.oztechan.tracefit.App
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController { App() }
