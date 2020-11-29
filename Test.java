/*
 * Test.java
 * 
 * Copyright 2020  <pi@raspberrypi>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 * 
 * 
 */


public class Test {
	
	public static void main (String[] args) {
		System.out.println("hello");
		StorageBox box = new StorageBox();
		System.out.println("set Up Storage Box ok");
		MotorWriter mW = new MotorWriter(20,box,"/dev/ttyAMA0");
		System.out.println("set Up motorwriter ok");
		System.out.println(mW.toString());
		mW.driveM1(1000);
		mW.driveM2(1000);
		mW.driveM3(1000);
	}
}

