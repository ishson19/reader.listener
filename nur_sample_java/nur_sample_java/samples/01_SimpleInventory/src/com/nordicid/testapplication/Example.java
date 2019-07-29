package com.nordicid.testapplication;

import com.nordicid.samples.common.SamplesCommon;

import java.math.BigInteger;

import org.epctagcoder.parse.SGTIN.ParseSGTIN;
import org.epctagcoder.result.SGTIN;
//import org.epctagcoder.parse.SSCC.*;
//import org.epctagcoder.result.SSCC;

import com.nordicid.nurapi.NurApi;
import com.nordicid.nurapi.NurApiListener;
import com.nordicid.nurapi.NurEventAutotune;
import com.nordicid.nurapi.NurEventClientInfo;
import com.nordicid.nurapi.NurEventDeviceInfo;
import com.nordicid.nurapi.NurEventEpcEnum;
import com.nordicid.nurapi.NurEventFrequencyHop;
import com.nordicid.nurapi.NurEventIOChange;
import com.nordicid.nurapi.NurEventInventory;
import com.nordicid.nurapi.NurEventNxpAlarm;
import com.nordicid.nurapi.NurEventProgrammingProgress;
import com.nordicid.nurapi.NurEventTagTrackingChange;
import com.nordicid.nurapi.NurEventTagTrackingData;
import com.nordicid.nurapi.NurEventTraceTag;
import com.nordicid.nurapi.NurEventTriggeredRead;
import com.nordicid.nurapi.NurRespInventory;
import com.nordicid.nurapi.NurTag;



/**
 * This example shows how to run single synchronous inventory command.
 * - Inventory is used to read multiple tag's EPC codes in reader field of view
 */
public class Example {
	
	public static void main(String[] args) {
		
		NurApi api = null;
		
		try {
			// Create and connect new NurApi object
			// To change connection parameters, please modify SamplesCommon.java
			api = SamplesCommon.createAndConnectNurApi();
			api.setListener(apiListener);
		} 
		catch (Exception ex) {
			ex.printStackTrace();
			return;
		}
		
		try {
			// Clear tag storage
			api.clearIdBuffer(true);
			
			// Perform inventory w/ default settings
			// NOTE: Depending on settings this might take up to few seconds to finish
			NurRespInventory resp = api.inventory();
			System.out.println("inventory numTagsFound: " + resp.numTagsFound);
			
			if (resp.numTagsFound > 0)
			{
				// Fetch and print tags
				api.fetchTags();
				for (int n=0; n<api.getStorage().size(); n++) {
					NurTag tag = api.getStorage().get(n);
					System.out.println(String.format("tag[%d] EPC '%s' RSSI %d", n, tag.getEpcString(), tag.getRssi()));
					
					try {
	                     String UPC = "";
	                     String hex = tag.getEpcString();//30140087801CCDEFEDDB2AA1";
	                     if (hex.startsWith("3BE100"))
	                     {
	                           String  binaryValue = hexToBin(hex);
	                           UPC = ConvertToInHouseEpc(binaryValue);

	                     }
	                     else
	                     {
	                     ParseSGTIN parseSGTIN = ParseSGTIN.Builder()
	                           .withRFIDTag(hex)
	                           .build();
	                   SGTIN sgtin = parseSGTIN.getSGTIN();
	                   UPC = sgtin.getCompanyPrefix() + sgtin.getItemReference() + sgtin.getCheckDigit();
	                     }
	               System.out.println("UPC:  "+ UPC);
	              }

	              catch (Exception ex) {

	                     ex.printStackTrace();

	                     return;

	              }
					
				}
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}

		System.out.println("See you again!.");
		try {
			// Disconnect the connection
			api.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Dispose the NurApi
		api.dispose();
	}
	private static String ConvertToInHouseEpc(String binaryValue)

    {
        String header, commisioningAuth, itemNumber, serialNumber;
        if (binaryValue.length()< 96)
        {
             binaryValue = String.format("%1$" + 96 + "s", binaryValue).replace(' ', '0');
                           //binaryValue.PadLeft(96, '0');
        }
        //header = binaryValue.substring(0, 12); //12 bits 0 - 11
        //commisioningAuth = binaryValue.substring(12, 4); //4 bits 12 - 15
        itemNumber = binaryValue.substring(16, 48+16); //48 bits 16 - 63
        //serialNumber = binaryValue.substring(64, 32); //38 bits 64 - 95
        //tr.Decimal = Convert.ToInt64(header, 2) + "." + Convert.ToInt64(commisioningAuth, 2) + "." + Convert.ToInt64(itemNumber, 2) + "." + Convert.ToInt64(serialNumber, 2);
        String Upc = Long.toString(Long.parseUnsignedLong(itemNumber, 2)); //GetUpcForInhouseTag(Convert.ToInt64(itemNumber, 2).ToString());
        //tr.Epc = tr.TagPrefix() + "." + Convert.ToInt64(itemNumber, 2) + "." + Convert.ToInt64(serialNumber, 2);
        return Upc;

    }
       static String hexToBin(String s) {
                return new BigInteger(s, 16).toString(2);
              }
	
	static NurApiListener apiListener = new NurApiListener() {
		
		@Override
		public void triggeredReadEvent(NurEventTriggeredRead arg0) {
		}
		
		@Override
		public void traceTagEvent(NurEventTraceTag arg0) {
		}
		
		@Override
		public void programmingProgressEvent(NurEventProgrammingProgress arg0) {
		}
		
		@Override
		public void logEvent(int arg0, String arg1) {
		}
		
		@Override
		public void inventoryStreamEvent(NurEventInventory arg0) {
		}
		
		@Override
		public void inventoryExtendedStreamEvent(NurEventInventory arg0) {
		}
		
		@Override
		public void frequencyHopEvent(NurEventFrequencyHop arg0) {
		}
		
		@Override
		public void disconnectedEvent() {
		}
		
		@Override
		public void deviceSearchEvent(NurEventDeviceInfo arg0) {
		}
		
		@Override
		public void debugMessageEvent(String arg0) {
		}
		
		@Override
		public void connectedEvent() {
		}
		
		@Override
		public void clientDisconnectedEvent(NurEventClientInfo arg0) {
		}
		
		@Override
		public void clientConnectedEvent(NurEventClientInfo arg0) {
		}
		
		@Override
		public void bootEvent(String arg0) {
		}
		
		@Override
		public void IOChangeEvent(NurEventIOChange arg0) {
		}

		@Override
		public void autotuneEvent(NurEventAutotune arg0) {
		}

		@Override
		public void epcEnumEvent(NurEventEpcEnum arg0) {
		}

		@Override
		public void nxpEasAlarmEvent(NurEventNxpAlarm arg0) {
		}

		@Override
		public void tagTrackingChangeEvent(NurEventTagTrackingChange arg0) {
		}

		@Override
		public void tagTrackingScanEvent(NurEventTagTrackingData arg0) {
		}
	};
}
