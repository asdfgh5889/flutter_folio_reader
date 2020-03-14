import 'dart:async';

import 'package:flutter/services.dart';

class FlutterFolioReader {
  static const MethodChannel _channel =
      const MethodChannel('flutter_folio_reader');

  static Future<void> openBook(String path) async {
    await _channel.invokeMethod('openBook', path);
  }
}
