package uz.raisense.flutter_folio_reader

import androidx.annotation.NonNull;
import com.folioreader.Config
import com.folioreader.FolioReader
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry.Registrar

/** FlutterFolioReaderPlugin */
class FlutterFolioReaderPlugin: FlutterPlugin, MethodCallHandler {
  lateinit var reader: FolioReader
  val config: Config = Config()

  override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
    val channel = MethodChannel(flutterPluginBinding.binaryMessenger, "flutter_folio_reader")
    channel.setMethodCallHandler(FlutterFolioReaderPlugin())
  }

  companion object {
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "flutter_folio_reader")
      channel.setMethodCallHandler(FlutterFolioReaderPlugin())
    }
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "openBook") {
      reader = FolioReader.get()
      config.allowedDirection = Config.AllowedDirection.VERTICAL_AND_HORIZONTAL
      reader.setConfig(config, true)
      reader.openBook(call.arguments as String)
      result.success(true)
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
  }
}
