import org.arl.fjage.Message
import org.arl.unet.*
import org.arl.fjage.*
import org.arl.unet.PDU
import java.util.*
import org.arl.unet.phy.*
class Test_Agent extends UnetAgent {

  private AgentID phy
  final static int cluster_protocol = Protocol.USER
  final static int cluster_protocol1 = Protocol.DATA
  private AgentID node
  ArrayList<Integer> neighbouraddr1=new ArrayList<Integer>()
  ArrayList<Integer> collectB=new ArrayList<Integer>()
  
  def datapacket = PDU.withFormat {
     length(16)                     // 16 byte PDU {protocol data unit}
     uint8('Type')                  //datapacket of 16 bytes
     uint8('CHaddr')             
     padding(0xff)                  // padded with 0xff to make 16 bytes
   }
 def addr=0
  void startup() 
  {   
     neighbouraddr1[0]=51;
  
     def phy = agentForService Services.PHYSICAL    //to communicate between two nodes
         subscribe topic(phy)
  
     def node = agentForService(Services.NODE_INFO)
        addr = node.Address
  
     if(addr==31)
     {
            
        add new WakerBehavior(100000, {
              phy.maxPowerLevel = 200.dB;
             phy[1].powerLevel = 125.dB;    // must be non- positive
             phy << new ClearReq()
             phy << new DatagramReq(to: 21 ,data : neighbouraddr1 , protocol:cluster_protocol)  
             println "data sent by 31 to 21"
        
        })
      
     }
  }

  void processMessage(Message msg) {
      if(addr == 21)
      {
          println "message processed in address:"+addr 
         if (msg instanceof DatagramNtf && msg.protocol == cluster_protocol  && msg.from == 31 )      //notfication recieved
         {
              println "DATA Received by ${addr} from ${msg.from}, DATA VALUE is ${msg.data}"
              collectB.addAll(msg.data)
              println "now total value is ${collectB}"
         }
      }
  }
}