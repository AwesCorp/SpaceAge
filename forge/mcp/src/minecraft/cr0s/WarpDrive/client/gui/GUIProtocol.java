package cr0s.WarpDrive.client.gui;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import uedevkit.gui.GuiSimpleBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cr0s.WarpDrive.WarpDrive;
import cr0s.WarpDrive.WarpDriveConfig;
import cr0s.WarpDrive.client.ClientProxy;
import cr0s.WarpDrive.common.container.ContainerProtocol;
import cr0s.WarpDrive.tile.TileEntityProtocol;

	/**
	 *The GUI for the Protocol (Warp Interface) block. 
	 *@author SkylordJoel
	 */

	@SideOnly(Side.CLIENT)
	public class GUIProtocol extends GuiSimpleBase { 
		/*TODO
		 * List of stuff:
		 * dim_getP - done
		 * dim_setP - done
		 * dim_getN - done
		 * dim_setN - done
		 * setMode - done
		 * setDistance - done
		 * setDirection - done
		 * getAttachedPlayers - done
		 * summon
		 * summonAll - done
		 * getX - done
		 * getY - done
		 * getZ - done
		 * energyRemaining For Core
		 * doJump - done
		 * getShipSize - done
		 * setBeaconFrequency - unused
		 * getDx - done
		 * getDz - dome
		 * setCoreFrequency - more like ship name, done	
		 * isInSpace - done
		 * isInHyperspace - done
		 * setTargetJumpgate*/
	   
		private static final ResourceLocation furnaceGuiTextures = new ResourceLocation(WarpDrive.modid + ":textures/gui/" + "guiWarpInterface_alternate.png");//ClientProxy.warpInterfaceGui/*"textures/gui/container/furnace.png"*/);
	    private TileEntityProtocol furnaceInventory;
	    
	    private GuiTextField front;
	    private GuiTextField back;
	    private GuiTextField left;
	    private GuiTextField right;
	    private GuiTextField up;
	    private GuiTextField down;
	    
	    private GuiTextField distance;
	    
	    private GuiTextField mode;
	    
	    private GuiTextField direction;
	    
	    private GuiTextField shipName;
	    //private GuiTextField beaconInput; UNUSED
	    
	    private char[] allowedChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	    private char[] modeChars = { '0'/*jump/long jump*/, '1'/*To hyperspace*/, '2'/*jumpgate*//*, '3'/*jump to different dimensions*/ };
	    private char[] directionChars = { '0'/*up*/, '1'/*down*/, '2'/*left*/, '3'/*right*/, '4'/*forward*/, '5'/*back*/ };

	    //TEST
	    public static final GuiRectangle frontSlider = new GuiRectangle(0, 0, 0, 0);
	    private boolean isDragging;
	    
	    public GUIProtocol(InventoryPlayer par1InventoryPlayer, TileEntityProtocol tile_entity) {
	        super(new ContainerProtocol(par1InventoryPlayer, tile_entity));
	        this.furnaceInventory = tile_entity;
	        
	        this.xSize = 256;
	        this.ySize = 256;
	    }
	    
	    @Override
	    public void mouseClick(GUIProtocol gui, int x, int y, int button) {
	    	
	    }
	    
	    int grey = 4210752;
	    
	    int sizex = 26;//10-24
	    int sizey = 33;
	    
	    int dimensionx = 15;
	    int dimensiony = 177;
	    
	    int blocksx = 26;
	    int blocksy = 64;
	    
	    int coord0x = 26;
	    int coord0y = 75;
	    
	    int pixelsPerWord = 10;
	    
	    String size0 = "Amount of blocks from Core to be moved: ";
	    
	    
	    /**
	     * Draw the foreground layer for the GuiContainer (everything in front of the items)
	     */
	    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
	        String s = this.furnaceInventory.isInvNameLocalized() ? this.furnaceInventory.getInvName() : I18n.getString(this.furnaceInventory.getInvName());
	        this.fontRenderer.drawString(s, this.xSize / 2 - this.fontRenderer.getStringWidth(s) / 2, 6, 4210752);
	        this.fontRenderer.drawString(I18n.getString("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
	        
	        String size1 = furnaceInventory.getFront() + ", Right: " + furnaceInventory.getRight() + ", Up: " + furnaceInventory.getUp();
		    String size2 = "Back: " + furnaceInventory.getBack() + ", Left: " + furnaceInventory.getLeft() + ", Down: " + furnaceInventory.getDown();

		    String dimension = furnaceInventory.currentDimension();
		    
		    String blocks = "Ship Size: " + String.valueOf(furnaceInventory.getShipSize());
		    
		    String coord0 = "Coordinates: ";
		    String coord1 = "X: " + String.valueOf(furnaceInventory.getCoreX());
		    String coord2 = "Y: " + String.valueOf(furnaceInventory.getCoreY());
		    String coord3 = "Z: " + String.valueOf(furnaceInventory.getCoreZ());
		    
		    String coord4 = "Destination Coordinates: ";
		    String coord5 = "X: " + String.valueOf(furnaceInventory.getDestX());
		    //String coord6 = "Y: " + String.valueOf(furnaceInventory.getDestY());
		    String coord7 = "Z: " + String.valueOf(furnaceInventory.getDestZ());
	        
	        write(size0, sizex, sizey, grey); //Title
	        write(size1, sizex, sizey + pixelsPerWord, grey); //setP
	        write(size2, sizex, sizey + (2 * pixelsPerWord), grey); //setN
	        
	        write(dimension, dimensionx, dimensiony, grey);
	        
	        write(coord0, coord0x, coord0y, grey);
	        write(coord1, coord0x, coord0y + pixelsPerWord, grey);
	        write(coord2, coord0x, coord0y + 2 * (pixelsPerWord), grey);
	        write(coord3, coord0x, coord0y + 3 * (pixelsPerWord), grey);
	        
	        write(coord4, coord0x, coord0y + 5 * (pixelsPerWord), grey);
	        write(coord5, coord0x, coord0y + 6 * (pixelsPerWord), grey);
	        write(coord7, coord0x, coord0y + 7 * (pixelsPerWord), grey);
	        
	        write(blocks, blocksx, blocksy, grey);
	        
	        front.drawTextBox();
	        back.drawTextBox();
	        left.drawTextBox();
	        right.drawTextBox();
	        up.drawTextBox();
	        down.drawTextBox();
	        
	        distance.drawTextBox();
	        
	        mode.drawTextBox();
	        
	        direction.drawTextBox();
	        
	        shipName.drawTextBox();
	        
	        //beaconInput.drawTextBox(); UNUSED
	    }

	    /**
	     * Draw the background layer for the GuiContainer (everything behind the items)
	     */
	    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
	        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	        this.mc.getTextureManager().bindTexture(furnaceGuiTextures);
	        int k = (this.width - this.xSize) / 2;
	        int l = (this.height - this.ySize) / 2;
	        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	        
	        int i1;
	        
	        frontSlider.draw(this, 0, 0);
	        
	        //FLAME - DON'T NEED
	        /*if (this.furnaceInventory.isBurning())
	        {
	            i1 = this.furnaceInventory.getBurnTimeRemainingScaled(12);
	            this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
	        }*/
	        
	        //GIANT ARROW - DON'T NEED
	        /*i1 = this.furnaceInventory.getCookProgressScaled(24);
	        this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);*/
	    }
	    
	    @Override
	    public void initGui() {
	    	super.initGui();
	    	buttonList.clear();
	    	
	    	Keyboard.enableRepeatEvents(true);
		    
		    //int biX = 0; UNUSED
		    //int biY = 0; UNUSED
	    	
		    int frontX = 0; //TODO
		    int frontY = 0;
		    
		    int diX = 0;
		    int diY = 0;
		    
		    int dirX = 0;
		    int dirY = 0;
		    
		    int moX = 0;
		    int moY = 0;
		    
		    int sNX = 98;
		    int sNY = 12;
		    
		    int inputLength = 30;
		    int inputWidth = 20;
		    
		    //applyBasicAttributes(beaconInput); UNUSED
		    
		    shipName = new GuiTextField(this.fontRenderer, sNX, sNY, inputLength * 2, inputWidth);
		    shipName.setMaxStringLength(10);
		    
		    direction = new GuiTextField(this.fontRenderer, dirX, dirY, inputLength, inputWidth);
		    direction.setMaxStringLength(1);
		    
		    mode = new GuiTextField(this.fontRenderer, moX, moY, inputLength, inputWidth);
		    mode.setMaxStringLength(1);

		    distance = new GuiTextField(this.fontRenderer, diX, diY, inputLength, inputWidth);
		    distance.setMaxStringLength(3);
		    
	    	front = new GuiTextField(this.fontRenderer, frontX, frontY, inputLength, inputWidth);
	    	front.setMaxStringLength(3);
	    	
	    	back = new GuiTextField(this.fontRenderer, frontX, frontY + pixelsPerWord, inputLength, inputWidth);
	    	back.setMaxStringLength(3);
	    	
	    	left = new GuiTextField(this.fontRenderer, frontX, frontY + (2 * pixelsPerWord), inputLength, inputWidth);
	    	left.setMaxStringLength(3);
	    	
	    	right = new GuiTextField(this.fontRenderer, frontX, frontY + (3 * pixelsPerWord), inputLength, inputWidth);
	    	right.setMaxStringLength(3);
	    	
	    	up = new GuiTextField(this.fontRenderer, frontX, frontY + (4 * pixelsPerWord), inputLength, inputWidth);
	    	up.setMaxStringLength(3);
	    	
	    	down = new GuiTextField(this.fontRenderer, frontX, frontY + (5 * pixelsPerWord), inputLength, inputWidth);
	    	down.setMaxStringLength(3);
	    	
		    applyBasicAttributes(front);
		    applyBasicAttributes(back);
		    applyBasicAttributes(right);
		    applyBasicAttributes(left);
		    applyBasicAttributes(up);
		    applyBasicAttributes(down);
		    
		    applyBasicAttributes(distance);
		    
		    applyBasicAttributes(mode);
		    
		    applyBasicAttributes(direction);
		    
		    applyBasicAttributes(shipName);
	    	
	    	//beaconInput = new GuiTextField(this.fontRenderer, biX, biY, inputLength, inputWidth); UNUSED
	    	//beaconInput.setMaxStringLength(2); UNUSED
	    	
	    	/**/buttonList.add(new GuiButton(0/*button number, maybe for mod, else gui*/, guiLeft + 14/*Location in relation to left in pixels*/, guiTop + 222/*Location in relation to top in pixels*/, 30/*Length in pixels*/, 20/*Height in pixels*/, "Jump"/*Text on button*/));
	    	/**/buttonList.add(new GuiButton(1/*button number, maybe for mod, else gui*/, guiLeft + 14/*Location in relation to left in pixels*/, guiTop + 202/*Location in relation to top in pixels*/, 60/*Length in pixels*/, 20/*Height in pixels*/, "Summon All"/*Text on button*/));
	    	buttonList.add(new GuiButton(2, guiLeft + 44/*217: -40*/, guiTop + 222/*227: -45*/, 30, 20, "Wiki"));
	    }
	    
	    public void applyBasicAttributes(GuiTextField field) {
	    	//if(field != null) {
		    	field.setEnableBackgroundDrawing(true);
		    	field.setFocused(false);
		    	field.setCanLoseFocus(true);
	    	//}
		}

		@Override
	    protected void keyTyped(char par1, int par2) {
			if(anyTextBoxFocused()) {
				if(par1 != 27) {
					if(shipName.textboxKeyTyped(par1, par2)) {
						shipName();
					}
				
					if(isValidChar(par1)) {
				//super.keyTyped(par1, par2);
						if(front.textboxKeyTyped(par1, par2)) {
							simpleTextPacket("WarpDrive_F", this.front, 4);
						} else if(back.textboxKeyTyped(par1, par2)) {
							simpleTextPacket("WarpDrive_B", this.back, 4);
						} else if(left.textboxKeyTyped(par1, par2)) {
							simpleTextPacket("WarpDrive_L", this.left, 4);
						} else if(right.textboxKeyTyped(par1, par2)) {
							simpleTextPacket("WarpDrive_R", this.right, 4);
						} else if(up.textboxKeyTyped(par1, par2)) {
							simpleTextPacket("WarpDrive_U", this.up, 4);
						} else if(down.textboxKeyTyped(par1, par2)) {
							simpleTextPacket("WarpDrive_D", this.down, 4);
						} else if(distance.textboxKeyTyped(par1, par2)) {
							simpleTextPacket("WarpDrive_Dis", this.distance, 4);
						} else if(isValidModeChar(par1)) {
							if(mode.textboxKeyTyped(par1, par2)) {
								modeTextPacket("WarpDrive_M", this.mode, 4);
							} else if(isValidDirectionChar(par1)) {
								if(direction.textboxKeyTyped(par1, par2)) {
									directionTextPacket("WarpDrive_Dir", this.direction, 4);
								}
							}
				//} else if(beaconInput.textboxKeyTyped(par1, par2)) {UNUSED
					//simpleTextPacket("WarpDrive_Protocol", this.beaconInput.getText().getBytes())); UNUSED
						}
					} else {
						super.keyTyped(par1, par2);
					}
				}
			} else if (par1 == 27) {
				mc.thePlayer.closeScreen();
			}
		}
		
	    public boolean anyTextBoxFocused() {
			return front.isFocused() || back.isFocused() || left.isFocused() || right.isFocused() || up.isFocused() || down.isFocused() || distance.isFocused() || mode.isFocused() || direction.isFocused() || shipName.isFocused();
		}

		public void shipName() {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(shipName.getText().length());
			DataOutputStream output = new DataOutputStream(bos);
			
			try {
				String f = shipName.getText();
				
				output.writeUTF(f);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = "WarpDrive_SN";
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			
			this.mc.thePlayer.sendQueue.addToSendQueue(packet);
		}

		public void directionTextPacket(String channel, GuiTextField field, int bytes) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes);
			DataOutputStream output = new DataOutputStream(bos);
			
			try {
				String f = field.getText();
				int fI = Integer.parseInt(f);
				
				output.writeInt(fI);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = channel;
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			
			this.mc.thePlayer.sendQueue.addToSendQueue(packet);
		}

		private boolean isValidDirectionChar(char par1) {
	    	for(int x = 0; x <= this.directionChars.length; x++) {
	    		if(par1 == this.directionChars[x]) {
	    			return true;
	    		}
	    	}
			return false;
		}

		private boolean isValidModeChar(char par1) {
	    	for(int x = 0; x <= this.modeChars.length; x++) {
	    		if(par1 == this.modeChars[x]) {
	    			return true;
	    		}
	    	}
	    	return false;
	    }

		public void modeTextPacket(String channel, GuiTextField field, int lengthOfBytesInField) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(lengthOfBytesInField);
			DataOutputStream output = new DataOutputStream(bos);
			
			try {
				String f = field.getText();
				int fI = Integer.parseInt(f);
				
				output.writeInt(fI);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = channel;
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			
			this.mc.thePlayer.sendQueue.addToSendQueue(packet);
		}

		public void simpleTextPacket(String channel, GuiTextField field, int lengthOfBytesInField) {
			ByteArrayOutputStream bos = new ByteArrayOutputStream(lengthOfBytesInField);
			DataOutputStream output = new DataOutputStream(bos);
			
			try {
				String f = field.getText();
				int fI = Integer.parseInt(f);
				
				output.writeInt(fI);
			} catch(Exception e) {
				e.printStackTrace();
			}
			
			Packet250CustomPayload packet = new Packet250CustomPayload();
			packet.channel = channel;
			packet.data = bos.toByteArray();
			packet.length = bos.size();
			
			this.mc.thePlayer.sendQueue.addToSendQueue(packet);
		}

		@Override
	    protected void mouseClicked(int x, int y, int buttonClicked) {
	    	super.mouseClicked(x, y, buttonClicked);
	    	
	    	this.front.mouseClicked(x, y, buttonClicked);
	    	this.back.mouseClicked(x, y, buttonClicked);
	    	this.left.mouseClicked(x, y, buttonClicked);
	    	this.right.mouseClicked(x, y, buttonClicked);
	    	this.up.mouseClicked(x, y, buttonClicked);
	    	this.down.mouseClicked(x, y, buttonClicked);
	    	
	    	this.distance.mouseClicked(x, y, buttonClicked);
	    	
	    	this.mode.mouseClicked(x, y, buttonClicked);
	    	
	    	this.direction.mouseClicked(x, y, buttonClicked);
	    	
	    	this.shipName.mouseClicked(x, y, buttonClicked);
	    	
	    	//this.beaconInput.mouseClicked(x, y, buttonClicked); UNUSED
	    }
	    
	    private boolean isValidChar(char par1) {
	    	for(int x = 0; x <= this.allowedChars.length; x++) {
	    		if(par1 == this.allowedChars[x]) {
	    			return true;
	    		}
	    	}
	    	return false;
	    }
	    
	    @Override
	    public void updateScreen() {
	    	this.front.updateCursorCounter();
	    	this.back.updateCursorCounter();
	    	this.left.updateCursorCounter();
	    	this.right.updateCursorCounter();
	    	this.up.updateCursorCounter();
	    	this.down.updateCursorCounter();
	    	
	    	this.distance.updateCursorCounter();
	    	
	    	this.mode.updateCursorCounter();
	    	
	    	this.direction.updateCursorCounter();
	    	
	    	this.shipName.updateCursorCounter();
	    	
	    	//this.beaconInput.updateCursorCounter(); UNUSED
	    }
	    
	    @Override
	    protected void actionPerformed(GuiButton button) {
	    	switch(button.id) {
	    		case 0:
	    			furnaceInventory.doJump();
	    			System.out.println(furnaceInventory.getCoreFrequency() + " jumped from coordinates " + String.valueOf(furnaceInventory.getCoreX()) + ", " + String.valueOf(furnaceInventory.getCoreY()) + ", " + String.valueOf(furnaceInventory.getCoreZ()));
	    			break;
	    		case 1:
	    			furnaceInventory.setSummonAllFlag(true);
	    			break;
	    			//System.out.println("Clicked!");//ACTION PERFORMED ON BUTTON CLICK
	    		case 2:
	    			openURL("https://sites.google.com/site/spaceageminecraft/wiki/blocks/warp-interface");
	    	}
	    }
	    
	    @Override
	    public void onGuiClosed() {
	    	super.onGuiClosed();
	    	Keyboard.enableRepeatEvents(false);
	    }
	    
	    @Override
	    public void drawScreen(int par1, int par2, float par3) {
	    	super.drawScreen(par1, par2, par3);
	    	this.front.drawTextBox();
	    	this.back.drawTextBox();
	    	this.left.drawTextBox();
	    	this.right.drawTextBox();
	    	this.up.drawTextBox();
	    	this.down.drawTextBox();
	    	
	    	this.distance.drawTextBox();
	    	
	    	this.mode.drawTextBox();
	    	
	    	this.direction.drawTextBox();
	    	
	    	this.shipName.drawTextBox();
	    }

		public int getLeft() {
			return guiLeft;
		}
		
		public int getTop() {
			return guiTop;
		}
	}