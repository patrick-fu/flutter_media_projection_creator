#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint media_projection_creator.podspec' to validate before publishing.
#
Pod::Spec.new do |s|
  s.name             = 'media_projection_creator'
  s.version          = '0.0.1'
  s.summary          = 'A flutter plugin of the creator used to create MediaProjection for Android'
  s.description      = <<-DESC
A flutter plugin of the creator used to create MediaProjection for Android
                       DESC
  s.homepage         = 'http://example.com'
  s.license          = { :file => '../LICENSE' }
  s.author           = { 'Your Company' => 'email@example.com' }
  s.source           = { :path => '.' }
  s.source_files = 'Classes/**/*'
  s.public_header_files = 'Classes/**/*.h'
  s.dependency 'Flutter'
  s.platform = :ios, '8.0'

  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }
end
