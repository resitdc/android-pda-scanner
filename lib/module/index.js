"use strict";

import { NativeEventEmitter, NativeModules } from "react-native";
const LINKING_ERROR = "The package react-native-pda-scanner does not seem to be linked.";
const PdaScanner = NativeModules.PdaScannerModule ? NativeModules.PdaScannerModule : new Proxy({}, {
  get() {
    throw new Error(LINKING_ERROR);
  }
});
const eventEmitter = new NativeEventEmitter(PdaScanner);
export function addBarcodeListener(callback) {
  return eventEmitter.addListener("onBarcodeScanned", rawString => {
    const parts = rawString.split(":::");
    const barcode = parts.length > 1 ? parts.slice(1).join(":::") : "";
    callback(barcode);
  });
}
//# sourceMappingURL=index.js.map