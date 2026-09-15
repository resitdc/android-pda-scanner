require "json"
package = JSON.parse(File.read(File.join(__dir__, "package.json")))
Pod::Spec.new do |s|
  s.name         = "react-native-pda-scanner"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.homepage     = package["homepage"] || "https://github.com/escapenomade/react-native-pda-scanner"
  s.license      = package["license"]
  s.authors      = package["author"]
  s.platforms    = { :ios => "11.0" }
  s.source       = { :git => "https://github.com/escapenomade/react-native-pda-scanner.git", :tag => "#{s.version}" }
  s.source_files = "ios/**/*.{h,m,mm,swift}"
  s.dependency "React-Core"
end
