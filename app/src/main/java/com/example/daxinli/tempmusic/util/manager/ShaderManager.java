package com.example.daxinli.tempmusic.util.manager;


import android.content.res.Resources;

import com.example.daxinli.tempmusic.util.ShaderUtil;

public class ShaderManager
{
	final static int shaderCount=7;
	final static String[][] shaderName=
	{
		{"vertex.sh","frag.sh"},
		{"vertex_load2d.sh","frag_load2d.sh"},
		{"vertex_2d.sh","frag_2d.sh"},
		{"holebox_vertex.sh","holebox_frag.sh"},
		{"vertex_lz.sh","frag_lz.sh"},
		{"vertex_spng.sh","frag_spng.sh"},
		{"vertex_rect.sh","frag_rect.sh"},		// 6

	};
	static String[]mVertexShader=new String[shaderCount];
	static String[]mFragmentShader=new String[shaderCount];
	static int[] program=new int[shaderCount];
	
	public static void loadCodeFromFile(Resources r)
	{
		for(int i=0;i<shaderCount;i++)
		{
			
	        mVertexShader[i]= ShaderUtil.loadFromAssetsFile(shaderName[i][0],r);
	        
	        mFragmentShader[i]=ShaderUtil.loadFromAssetsFile(shaderName[i][1], r);
		}	
	}
	
	
	public static void compileShader()
	{
		for(int i=0;i<shaderCount;i++)
		{
			program[i]=ShaderUtil.createProgram(mVertexShader[i], mFragmentShader[i]);
		}
	}
	
	public static int getShader(int index)
	{
		return program[index];
	}
		
}
