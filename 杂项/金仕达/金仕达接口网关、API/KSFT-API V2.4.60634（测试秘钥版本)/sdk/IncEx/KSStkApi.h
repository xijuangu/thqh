/////////////////////////////////////////////////////////////////////////
///@system 新一代交易系统
///@company SunGard China
///@file KSStkApi.h
///@brief 定义了客户端现货接口
/////////////////////////////////////////////////////////////////////////

#ifndef __KSSTKAPI_H_INCLUDED_
#define __KSSTKAPI_H_INCLUDED_

#if _MSC_VER > 1000
#pragma once
#endif // _MSC_VER > 1000

#include "KSUserApiStructEx.h"
#include "KSVocApiStruct.h"

#if defined(ISLIB) && defined(WIN32) && !defined(KSTRADEAPI_STATIC_LIB)

#ifdef LIB_TRADER_API_EXPORT
#define TRADER_STKAPI_EXPORT __declspec(dllexport)
#else
#define TRADER_STKAPI_EXPORT __declspec(dllimport)
#endif
#else
#ifdef WIN32
#define TRADER_STKAPI_EXPORT 
#else
#define TRADER_STKAPI_EXPORT __attribute__((visibility("default")))
#endif

#endif

namespace KingstarAPI
{

	class CKSStkSpi
	{
	public:
		///证券报单录入请求响应
		virtual void OnRspStkOrderInsert(CKSStkInputOrderField *pStkInputOrder, CThostFtdcRspInfoField *pRspInfo, int nRequestID, bool bIsLast) {};

		///证券报单操作请求响应
		virtual void OnRspStkOrderAction(CKSStkInputOrderActionField *pStkInputOrderAction, CThostFtdcRspInfoField *pRspInfo, int nRequestID, bool bIsLast) {};

		///请求查询证券报单响应
		virtual void OnRspQryStkOrder(CKSStkOrderField *pStkOrder, CThostFtdcRspInfoField *pRspInfo, int nRequestID, bool bIsLast) {};

		///请求查询证券成交明细响应
		virtual void OnRspQryStkTradeDetail(CKSStkTradeField *pStkTrade, CThostFtdcRspInfoField *pRspInfo, int nRequestID, bool bIsLast) {};

		///请求查询证券成交汇总响应
		virtual void OnRspQryStkTrade(CKSStkTradeResultField *pStkTradeResult, CThostFtdcRspInfoField *pRspInfo, int nRequestID, bool bIsLast) {};

		///请求查询证券余额响应
		virtual void OnRspQryStkBalance(CKSStkBalanceField *pStkBalance, CThostFtdcRspInfoField *pRspInfo, int nRequestID, bool bIsLast) {};

		///请求查询证券历史报单响应
		virtual void OnRspQryStkOrderHistorical(CKSStkOrderHistoricalField *pStkOrderHistorical, CThostFtdcRspInfoField *pRspInfo, int nRequestID, bool bIsLast) {};

		///请求查询证券信息响应
		virtual void OnRspQryStkSystemInfo(CKSStkSystemInfoField *pStkSystemInfo, CThostFtdcRspInfoField *pRspInfo, int nRequestID, bool bIsLast) {};
	
		///请求查询证券手续费率响应
		virtual void OnRspQryStkCommissionRate(CKSStkCommissionRateField *pStkCommissionRate, CThostFtdcRspInfoField *pRspInfo, int nRequestID, bool bIsLast) {};

		///证券发起银行资金转证券应答
		virtual void OnRspFromBankToStockByStock(CThostFtdcReqTransferField *pReqTransfer, CThostFtdcRspInfoField *pRspInfo, int nRequestID, bool bIsLast) {};

		///证券发起证券资金转银行应答
		virtual void OnRspFromStockToBankByStock(CThostFtdcReqTransferField *pReqTransfer, CThostFtdcRspInfoField *pRspInfo, int nRequestID, bool bIsLast) {};

		///证券发起查询银行余额应答
		virtual void OnRspQueryBankAccountMoneyByStock(CThostFtdcReqQueryAccountField *pReqQueryAccount, CThostFtdcRspInfoField *pRspInfo, int nRequestID, bool bIsLast) {};

		///请求查询证券签约银行响应
		virtual void OnRspQryStkContractBank(CThostFtdcContractBankField *pContractBank, CThostFtdcRspInfoField *pRspInfo, int nRequestID, bool bIsLast) {};

		///请求查询证券转帐流水响应
		virtual void OnRspQryStkTransferSerial(CThostFtdcTransferSerialField *pTransferSerial, CThostFtdcRspInfoField *pRspInfo, int nRequestID, bool bIsLast) {};
		
		///请求查询证券可提资金响应
		virtual void OnRspQryStkAvailableMoney(CKSStkAvailableMoneyField *pStkAvailableMoney, CThostFtdcRspInfoField *pRspInfo, int nRequestID, bool bIsLast) {};

		///证券发起查询银行余额通知
		virtual void OnRtnQueryBankBalanceByStock(CThostFtdcNotifyQueryAccountField *pNotifyQueryAccount) {};

		///证券报单通知
		virtual void OnRtnStkOrder(CKSStkOrderField *pStkOrder) {};

		///证券成交通知
		virtual void OnRtnStkTrade(CKSStkTradeField *pStkTrade) {};
	
	};

	class TRADER_STKAPI_EXPORT CKSStkApi
	{
	public:
		///用户登录请求
		virtual int ReqStkUserLogin(CThostFtdcReqUserLoginField *pReqUserLoginField, int nRequestID) = 0;

		///证券报单录入请求
		virtual int ReqStkOrderInsert(CKSStkInputOrderField *pStkInputOrder, int nRequestID) = 0;

		///证券报单操作请求
		virtual int ReqStkOrderAction(CKSStkInputOrderActionField *pStkInputOrderAction, int nRequestID) = 0;
		
		///请求查询报单
		virtual int ReqQryStkOrder(CKSQryStkOrderField *pQryStkOrder, int nRequestID) = 0;

		///请求查询成交明细
		virtual int ReqQryStkTradeDetail(CKSQryStkTradeDetailField *pQryStkTradeDetail, int nRequestID) = 0;

		///请求查询成交汇总
		virtual int ReqQryStkTrade(CKSQryStkTradeField *pQryStkTrade, int nRequestID) = 0;

		///请求查询证券余额
		virtual int ReqQryStkBalance(CKSQryStkBalanceField *pQryStkBalance, int nRequestID) = 0;

		///请求查询证券历史报单
		virtual int ReqQryStkOrderHistorical(CKSQryStkOrderHistoricalField *pQryStkOrderHistorical, int nRequestID) = 0;

		///请求查询证券信息
		virtual int ReqQryStkSystemInfo(CKSQryStkSystemInfoField *pQryStkSystemInfo, int nRequestID) = 0;
	
		///请求查询证券手续费率
		virtual int ReqQryStkCommissionRate(CKSQryStkCommissionRateField *pQryStkCommissionRate, int nRequestID) = 0;

		///证券发起银行资金转证券请求
		virtual int ReqFromBankToStockByStock(CThostFtdcReqTransferField *pReqTransfer, int nRequestID) = 0;

		///证券发起证券资金转银行请求
		virtual int ReqFromStockToBankByStock(CThostFtdcReqTransferField *pReqTransfer, int nRequestID) = 0;

		///证券发起查询银行余额请求
		virtual int ReqQueryBankAccountMoneyByStock(CThostFtdcReqQueryAccountField *pReqQueryAccount, int nRequestID) = 0;

		///请求查询证券签约银行
		virtual int ReqQryStkContractBank(CThostFtdcQryContractBankField *pQryContractBank, int nRequestID) = 0;

		///请求查询证券转账流水
		virtual int ReqQryStkTransferSerial(CThostFtdcQryTransferSerialField *pQryTransferSerial, int nRequestID) = 0;

		///请求查询证券可提资金
		virtual int ReqQryStkAvailableMoney(CKSQryStkAvailableMoneyField *pQryStkAvailableMoney, int nRequestID) = 0;

	protected:
		~CKSStkApi(){};
	};

}	// end of namespace KingstarAPI
#endif