#import "MediaProjectionCreatorPlugin.h"

@implementation MediaProjectionCreatorPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"media_projection_creator"
            binaryMessenger:[registrar messenger]];
  MediaProjectionCreatorPlugin* instance = [[MediaProjectionCreatorPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    result([FlutterError errorWithCode:[@"IOS_IS_NOT_SUPPORTED" uppercaseString] message:@"This plugin only support Android" details:nil]);
}

@end
