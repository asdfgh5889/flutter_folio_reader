#import "FlutterFolioReaderPlugin.h"
#if __has_include(<flutter_folio_reader/flutter_folio_reader-Swift.h>)
#import <flutter_folio_reader/flutter_folio_reader-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_folio_reader-Swift.h"
#endif

@implementation FlutterFolioReaderPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterFolioReaderPlugin registerWithRegistrar:registrar];
}
@end
