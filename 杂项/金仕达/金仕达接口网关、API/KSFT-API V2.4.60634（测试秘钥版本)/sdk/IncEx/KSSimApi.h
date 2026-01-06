/////////////////////////////////////////////////////////////////////////
///@system 新一代交易系统
///@company SunGard China
///@file KSSimApi.h
///@brief 定义了客户端模拟开户接口
/////////////////////////////////////////////////////////////////////////

#ifndef __KSSIMAPI_H_INCLUDED_
#define __KSSIMAPI_H_INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "KSUserApiStructEx.h"
#include "KSVocApiStruct.h"

#if defined(ISLIB) && defined(WIN32) && !defined(KSTRADEAPI_STATIC_LIB)

#ifdef LIB_TRADER_API_EXPORT
#define TRADER_SIMAPI_EXPORT __declspec(dllexport)
#else
#define TRADER_SIMAPI_EXPORT __declspec(dllimport)
#endif
#else
#ifdef WIN32
#define TRADER_SIMAPI_EXPORT 
#else
#define TRADER_SIMAPI_EXPORT __attribute__((visibility("default")))
#endif

#endif

namespace KingstarAPI
{

	class CKSSimSpi
	{
	public:
		//模拟交易注册请求响应
		virtual void OnRspRegisterSimUser(CKSRspRegisterSimUserField *pRspRegisterSimUser, CThostFtdcRspInfoField *pRspInfo, int nRequestID, bool bIsLast){};
		//模拟交易找回密码请求响应
		virtual void OnRspGetBackPassword(CKSRspGetBackPasswordField *pRspGetBackPassword, CThostFtdcRspInfoField *pRspInfo, int nRequestID, bool bIsLast){};
		
	};

	class TRADER_SIMAPI_EXPORT CKSSimApi
	{
	public:
		//模拟交易注册请求
		virtual int ReqRegisterSimUser(CKSRegisterSimUserField *pRegisterSimUser,  int nRequestID) = 0;
		//模拟交易找回密码
		virtual int ReqGetBackPassword(CKSGetBackPasswordField *pGetBackPassword, int nRequestID) = 0;

	protected:
		~CKSSimApi(){};
	};

}	// end of namespace KingstarAPI
#endif