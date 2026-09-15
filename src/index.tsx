import { NativeEventEmitter, NativeModules, EmitterSubscription } from "react-native";

const LINKING_ERROR = "The package react-native-pda-scanner does not seem to be linked.";
const PdaScanner = NativeModules.PdaScannerModule
  ? NativeModules.PdaScannerModule
  : new Proxy({}, { get() { throw new Error(LINKING_ERROR); } });

const eventEmitter = new NativeEventEmitter(PdaScanner);

export function addBarcodeListener(callback: (barcode: string) => void): EmitterSubscription {
  return eventEmitter.addListener("onBarcodeScanned", (rawString: string) => {
    const parts = rawString.split(":::");
    const barcode = parts.length > 1 ? parts.slice(1).join(":::") : "";
    callback(barcode);
  });
}
