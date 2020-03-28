package uz.raisense.flutter_folio_reader

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.annotation.NonNull;
import com.folioreader.Config
import com.folioreader.FolioReader
import com.folioreader.model.locators.ReadLocator
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
    context = flutterPluginBinding.applicationContext
    channel.setMethodCallHandler(FlutterFolioReaderPlugin())
  }

  companion object {
    lateinit var context: Context
    @JvmStatic
    fun registerWith(registrar: Registrar) {
      val channel = MethodChannel(registrar.messenger(), "flutter_folio_reader")
      context = registrar.activeContext()
      channel.setMethodCallHandler(FlutterFolioReaderPlugin())
    }
  }

  override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
    if (call.method == "openBook") {
      val path = call.arguments as String
      reader = FolioReader.get()
      config.allowedDirection = Config.AllowedDirection.VERTICAL_AND_HORIZONTAL
      config.direction = Config.Direction.HORIZONTAL
      reader.setConfig(config, true)
      val db = ReadLocatorContract(context)
      reader.setReadLocatorListener {
        readLocator ->
        if (readLocator.toJson() != null)
          db.saveState(readLocator.bookId, readLocator.toJson()!!)
      }

      reader.setOnHighlightListener { highlight, type ->
        val locatorJson = db.readState(highlight.bookId)
        if (locatorJson != null) {
          val locator = ReadLocator.fromJson(locatorJson)
          reader.setReadLocator(locator)
        }
      }

      reader.openBook(path)

      result.success(true)
    } else {
      result.notImplemented()
    }
  }

  override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
  }
}