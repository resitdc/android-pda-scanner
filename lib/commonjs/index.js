"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.addBarcodeListener = addBarcodeListener;
var _reactNative = require("react-native");
const LINKING_ERROR = "The package react-native-pda-scanner does not seem to be linked.";
const PdaScanner = _reactNative.NativeModules.PdaScannerModule ? _reactNative.NativeModules.PdaScannerModule : new Proxy({}, {
  get() {
    throw new Error(LINKING_ERROR);
  }
});
const eventEmitter = new _reactNative.NativeEventEmitter(PdaScanner);
function addBarcodeListener(callback) {
  return eventEmitter.addListener("onBarcodeScanned", rawString => {
    const parts = rawString.split(":::");
    const barcode = parts.length > 1 ? parts.slice(1).join(":::") : "";
    callback(barcode);
  });
}
//# sourceMappingURL=index.js.map