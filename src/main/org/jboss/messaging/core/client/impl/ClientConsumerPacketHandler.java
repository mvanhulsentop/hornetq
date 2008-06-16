package org.jboss.messaging.core.client.impl;

import org.jboss.messaging.core.logging.Logger;
import org.jboss.messaging.core.remoting.Packet;
import org.jboss.messaging.core.remoting.PacketHandler;
import org.jboss.messaging.core.remoting.PacketReturner;
import org.jboss.messaging.core.remoting.impl.wireformat.PacketImpl;
import org.jboss.messaging.core.remoting.impl.wireformat.ReceiveMessage;

/**
 * @author <a href="mailto:jmesnil@redhat.com">Jeff Mesnil</a>
 * @author <a href="mailto:tim.fox@jboss.com">Tim Fox</a>
 *
 * @version <tt>$Revision$</tt>
 *
 */
public class ClientConsumerPacketHandler implements PacketHandler
{
   private static final Logger log = Logger.getLogger(ClientConsumerPacketHandler.class);

   private final ClientConsumerInternal clientConsumer;

   private final long consumerID;

   public ClientConsumerPacketHandler(final ClientConsumerInternal clientConsumer, final long consumerID)
   {
      this.clientConsumer = clientConsumer;
      
      this.consumerID = consumerID;
   }

   public long getID()
   {
      return consumerID;
   }

   public void handle(final Packet packet, final PacketReturner sender)
   {
      byte type = packet.getType();
      
      if (type == PacketImpl.RECEIVE_MSG)
      {
         ReceiveMessage message = (ReceiveMessage) packet;
         
         try
         {
            clientConsumer.handleMessage(message.getClientMessage());
         }
         catch (Exception e)
         {
            log.error("Failed to handle packet " + packet);
         }
      }
      else
      {
      	throw new IllegalStateException("Invalid packet: " + type);
      }
   }

   @Override
   public String toString()
   {
      return "ClientConsumerPacketHandler[id=" + consumerID + "]";
   }
   
   public boolean equals(Object other)
   {
      if (other instanceof ClientConsumerPacketHandler == false)
      {
         return false;
      }
            
      ClientConsumerPacketHandler r = (ClientConsumerPacketHandler)other;
      
      return r.consumerID == this.consumerID;     
   }
}