package com.techelevator.Java_Synth_2;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.activation.MailcapCommandMap;
import javax.swing.*;

import com.sun.tools.javac.parser.Scanner;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.data.Buffer;
import net.beadsproject.beads.ugens.Gain;
import net.beadsproject.beads.ugens.Glide;
import net.beadsproject.beads.ugens.WavePlayer;

public class Hello_Midi implements KeyListener {
	
	WavePlayer sine;
	WavePlayer sineTwo;
	Gain sineGain;
	Gain sineGainTwo;
	Glide gainGlide;
	JFrame frame;
	AudioContext ac;
	Scanner myScanner;
	Map<String, String> map;
	boolean s1=false;
	boolean s2=false;
	boolean toggle;
	
	
	public static void main(String[] args) throws IOException
	{
		Hello_Midi synth = new Hello_Midi();
		synth.setup();
	}
	
	// construct the synthesizer
	public void setup() throws IOException
	{
		  // create an AudioContext
		  ac = new AudioContext();
		 
		  // create a sine generator
		  sine = new WavePlayer(ac, 440.0f, Buffer.SAW);
		  sineTwo = new WavePlayer(ac, 880.0f, Buffer.SAW);
		  
		  // create a Glide to control the gain 
		  gainGlide = new Glide(ac, 0.0f, 100.0f);
		  
		  // create a Gain to control the sine volume
		  sineGain = new Gain(ac, 1, gainGlide);
		  
		  
		  // add the sine generator as an input to the Gain
		  sineGain.addInput(sine);
		  sineGain.addInput(sineTwo);
		 
		 
		  
		  // add the Gain as an input to the master output, ac.out
		  ac.out.addInput(sineGain);
		  
		 
		  
		  ac.start();
		  
		  //open frame
		  keyDemo();
		  
		  //populate notes MAP from CSV
		  keyToFrequencyFileReader();
		  
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int compKey = e.getKeyCode();
		int octave = e.getKeyCode();
		int sineSquare = e.getKeyCode();
		
		 
		Map<Integer, Float> hashMap =
				map.entrySet().stream().collect(Collectors.toMap(
                        entry -> Integer.parseInt(entry.getKey()),
                        entry -> Float.parseFloat(entry.getValue()))
                    );
		for (Map.Entry mapElement : hashMap.entrySet()) { 
			int note = (int) mapElement.getKey(); 
			Float frequency = ((Float)mapElement.getValue()); 
			
			if(sineSquare == KeyEvent.VK_1 ) {
				sine.setBuffer(Buffer.SINE);
				frame.getContentPane().setBackground(Color.magenta);
			}
			if(sineSquare == KeyEvent.VK_2 ) {
				sine.setBuffer(Buffer.SQUARE);
				frame.getContentPane().setBackground(Color.GREEN);
			}
			if(sineSquare == KeyEvent.VK_3 ) {
				sine.setBuffer(Buffer.SAW);
				frame.getContentPane().setBackground(Color.BLUE);
			}
			
			if(compKey == note) {
				s1 = true;
				gainGlide.setValue(0.9f);
				sine.setFrequency(frequency);
				sineTwo.setFrequency(frequency / 2);
				frame.getContentPane().setBackground(Color.CYAN);
			
			}
			if(octave == KeyEvent.VK_SHIFT) {
				s2 = true;
			}
			if(s1==true && s2==true && compKey == note) {
				gainGlide.setValue(0.9f);
				sine.setFrequency(frequency * 2);
				frame.getContentPane().setBackground(Color.yellow);
			}
			if(sineSquare == KeyEvent.VK_ENTER) {
				System.exit(0);
			}
			if(compKey == KeyEvent.VK_UP) {
				gainGlide.setGlideTime(gainGlide.getGlideTime() + 50.00f);
			}
			if(compKey == KeyEvent.VK_DOWN) {
				gainGlide.setGlideTime(gainGlide.getGlideTime() - 50.00f);
			}
			
		} 
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int compKey = e.getKeyCode();
		int octave = e.getKeyCode();
		int sineSquare = e.getKeyCode();
		
		Map<Integer, Float> hashMap =
				map.entrySet().stream().collect(Collectors.toMap(
                        entry -> Integer.parseInt(entry.getKey()),
                        entry -> Float.parseFloat(entry.getValue()))
                    );
		for (Map.Entry mapElement : hashMap.entrySet()) { 
			int note = (int) mapElement.getKey(); 
			
			if(compKey == note) {
				gainGlide.setValue(0.0f);
				frame.getContentPane().setBackground(Color.PINK);
				s1 = false;
			}
			if(octave == KeyEvent.VK_SHIFT) {
				s2 = false;
			}
			if(sineSquare == KeyEvent.VK_ALT && toggle == true) {
				toggle = false;
		
			}
		} 
	}
	public void keyDemo(){
		
		
		frame = new JFrame();
		frame.setSize( 400, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.addKeyListener(this);
		
	}
	public Map<String, String> keyToFrequencyFileReader() throws IOException {
	
			String line = "";
			File sinePatch = new File("sinePatch.csv");
			BufferedReader br = new BufferedReader(new FileReader(sinePatch));
			
			map = new HashMap<>();
			
			while((line = br.readLine()) != null) {
				String[] value = line.split(",");
				
				map.put((value[0]), (value[1]));
				
			}
		
			return map;
			
			
	}
	
}