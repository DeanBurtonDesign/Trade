
/**
 * ServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

    package com.cleartraders.autotrader.gainwsapi;

    /**
     *  ServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class ServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public ServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public ServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for GetRates method
            * override this method for handling normal response from GetRates operation
            */
           public void receiveResultGetRates(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetRatesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetRates operation
           */
            public void receiveErrorGetRates(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetPositionBlotterDataSet method
            * override this method for handling normal response from GetPositionBlotterDataSet operation
            */
           public void receiveResultGetPositionBlotterDataSet(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetPositionBlotterDataSetResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetPositionBlotterDataSet operation
           */
            public void receiveErrorGetPositionBlotterDataSet(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for PlaceIfThenOCOOrder method
            * override this method for handling normal response from PlaceIfThenOCOOrder operation
            */
           public void receiveResultPlaceIfThenOCOOrder(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.PlaceIfThenOCOOrderResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from PlaceIfThenOCOOrder operation
           */
            public void receiveErrorPlaceIfThenOCOOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for PlaceIfThenOrder method
            * override this method for handling normal response from PlaceIfThenOrder operation
            */
           public void receiveResultPlaceIfThenOrder(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.PlaceIfThenOrderResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from PlaceIfThenOrder operation
           */
            public void receiveErrorPlaceIfThenOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetDelayedRatesDataSet method
            * override this method for handling normal response from GetDelayedRatesDataSet operation
            */
           public void receiveResultGetDelayedRatesDataSet(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetDelayedRatesDataSetResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetDelayedRatesDataSet operation
           */
            public void receiveErrorGetDelayedRatesDataSet(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetProductSubscriptionRelationshipBlotterDataset method
            * override this method for handling normal response from GetProductSubscriptionRelationshipBlotterDataset operation
            */
           public void receiveResultGetProductSubscriptionRelationshipBlotterDataset(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetProductSubscriptionRelationshipBlotterDatasetResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetProductSubscriptionRelationshipBlotterDataset operation
           */
            public void receiveErrorGetProductSubscriptionRelationshipBlotterDataset(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for PlaceOCOASSPOrder method
            * override this method for handling normal response from PlaceOCOASSPOrder operation
            */
           public void receiveResultPlaceOCOASSPOrder(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.PlaceOCOASSPOrderResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from PlaceOCOASSPOrder operation
           */
            public void receiveErrorPlaceOCOASSPOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetSubAccountAuthenticationKey method
            * override this method for handling normal response from GetSubAccountAuthenticationKey operation
            */
           public void receiveResultGetSubAccountAuthenticationKey(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetSubAccountAuthenticationKeyResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetSubAccountAuthenticationKey operation
           */
            public void receiveErrorGetSubAccountAuthenticationKey(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for PlaceSingleASSPOrder method
            * override this method for handling normal response from PlaceSingleASSPOrder operation
            */
           public void receiveResultPlaceSingleASSPOrder(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.PlaceSingleASSPOrderResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from PlaceSingleASSPOrder operation
           */
            public void receiveErrorPlaceSingleASSPOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetRatesServerAuth method
            * override this method for handling normal response from GetRatesServerAuth operation
            */
           public void receiveResultGetRatesServerAuth(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetRatesServerAuthResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetRatesServerAuth operation
           */
            public void receiveErrorGetRatesServerAuth(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for DealRequestByDealId method
            * override this method for handling normal response from DealRequestByDealId operation
            */
           public void receiveResultDealRequestByDealId(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.DealRequestByDealIdResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from DealRequestByDealId operation
           */
            public void receiveErrorDealRequestByDealId(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetRatesBlotter method
            * override this method for handling normal response from GetRatesBlotter operation
            */
           public void receiveResultGetRatesBlotter(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetRatesBlotterResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetRatesBlotter operation
           */
            public void receiveErrorGetRatesBlotter(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ModifySingleASSPOrder method
            * override this method for handling normal response from ModifySingleASSPOrder operation
            */
           public void receiveResultModifySingleASSPOrder(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.ModifySingleASSPOrderResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ModifySingleASSPOrder operation
           */
            public void receiveErrorModifySingleASSPOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetTime method
            * override this method for handling normal response from GetTime operation
            */
           public void receiveResultGetTime(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetTimeResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetTime operation
           */
            public void receiveErrorGetTime(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ModifyOCOASSPOrder method
            * override this method for handling normal response from ModifyOCOASSPOrder operation
            */
           public void receiveResultModifyOCOASSPOrder(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.ModifyOCOASSPOrderResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ModifyOCOASSPOrder operation
           */
            public void receiveErrorModifyOCOASSPOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for Echo method
            * override this method for handling normal response from Echo operation
            */
           public void receiveResultEcho(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.EchoResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from Echo operation
           */
            public void receiveErrorEcho(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetCommentDataSet method
            * override this method for handling normal response from GetCommentDataSet operation
            */
           public void receiveResultGetCommentDataSet(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetCommentDataSetResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetCommentDataSet operation
           */
            public void receiveErrorGetCommentDataSet(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for DealRequest method
            * override this method for handling normal response from DealRequest operation
            */
           public void receiveResultDealRequest(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.DealRequestResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from DealRequest operation
           */
            public void receiveErrorDealRequest(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetPairSettings method
            * override this method for handling normal response from GetPairSettings operation
            */
           public void receiveResultGetPairSettings(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetPairSettingsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetPairSettings operation
           */
            public void receiveErrorGetPairSettings(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetSymbolBlotterDataSet method
            * override this method for handling normal response from GetSymbolBlotterDataSet operation
            */
           public void receiveResultGetSymbolBlotterDataSet(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetSymbolBlotterDataSetResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetSymbolBlotterDataSet operation
           */
            public void receiveErrorGetSymbolBlotterDataSet(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetMarginBlotterDataSet method
            * override this method for handling normal response from GetMarginBlotterDataSet operation
            */
           public void receiveResultGetMarginBlotterDataSet(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetMarginBlotterDataSetResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetMarginBlotterDataSet operation
           */
            public void receiveErrorGetMarginBlotterDataSet(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for PlaceSingleOrder method
            * override this method for handling normal response from PlaceSingleOrder operation
            */
           public void receiveResultPlaceSingleOrder(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.PlaceSingleOrderResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from PlaceSingleOrder operation
           */
            public void receiveErrorPlaceSingleOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetDealBlotter method
            * override this method for handling normal response from GetDealBlotter operation
            */
           public void receiveResultGetDealBlotter(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetDealBlotterResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetDealBlotter operation
           */
            public void receiveErrorGetDealBlotter(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for CancelOrderByOrderID method
            * override this method for handling normal response from CancelOrderByOrderID operation
            */
           public void receiveResultCancelOrderByOrderID(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.CancelOrderByOrderIDResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from CancelOrderByOrderID operation
           */
            public void receiveErrorCancelOrderByOrderID(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetHistoricRatesDataSet method
            * override this method for handling normal response from GetHistoricRatesDataSet operation
            */
           public void receiveResultGetHistoricRatesDataSet(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetHistoricRatesDataSetResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetHistoricRatesDataSet operation
           */
            public void receiveErrorGetHistoricRatesDataSet(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ModifySingleOrder method
            * override this method for handling normal response from ModifySingleOrder operation
            */
           public void receiveResultModifySingleOrder(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.ModifySingleOrderResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ModifySingleOrder operation
           */
            public void receiveErrorModifySingleOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetProductSubscriptionRelationshipBlotter method
            * override this method for handling normal response from GetProductSubscriptionRelationshipBlotter operation
            */
           public void receiveResultGetProductSubscriptionRelationshipBlotter(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetProductSubscriptionRelationshipBlotterResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetProductSubscriptionRelationshipBlotter operation
           */
            public void receiveErrorGetProductSubscriptionRelationshipBlotter(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for CancelOrder method
            * override this method for handling normal response from CancelOrder operation
            */
           public void receiveResultCancelOrder(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.CancelOrderResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from CancelOrder operation
           */
            public void receiveErrorCancelOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetNewsDataSet method
            * override this method for handling normal response from GetNewsDataSet operation
            */
           public void receiveResultGetNewsDataSet(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetNewsDataSetResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetNewsDataSet operation
           */
            public void receiveErrorGetNewsDataSet(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetEconomicCalendar method
            * override this method for handling normal response from GetEconomicCalendar operation
            */
           public void receiveResultGetEconomicCalendar(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetEconomicCalendarResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetEconomicCalendar operation
           */
            public void receiveErrorGetEconomicCalendar(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetNews method
            * override this method for handling normal response from GetNews operation
            */
           public void receiveResultGetNews(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetNewsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetNews operation
           */
            public void receiveErrorGetNews(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetRatesDataSet method
            * override this method for handling normal response from GetRatesDataSet operation
            */
           public void receiveResultGetRatesDataSet(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetRatesDataSetResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetRatesDataSet operation
           */
            public void receiveErrorGetRatesDataSet(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ModifyIfThenOCOOrder method
            * override this method for handling normal response from ModifyIfThenOCOOrder operation
            */
           public void receiveResultModifyIfThenOCOOrder(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.ModifyIfThenOCOOrderResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ModifyIfThenOCOOrder operation
           */
            public void receiveErrorModifyIfThenOCOOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetOrderBlotterDataSet method
            * override this method for handling normal response from GetOrderBlotterDataSet operation
            */
           public void receiveResultGetOrderBlotterDataSet(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetOrderBlotterDataSetResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetOrderBlotterDataSet operation
           */
            public void receiveErrorGetOrderBlotterDataSet(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetComment method
            * override this method for handling normal response from GetComment operation
            */
           public void receiveResultGetComment(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetCommentResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetComment operation
           */
            public void receiveErrorGetComment(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for PlaceOCOOrder method
            * override this method for handling normal response from PlaceOCOOrder operation
            */
           public void receiveResultPlaceOCOOrder(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.PlaceOCOOrderResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from PlaceOCOOrder operation
           */
            public void receiveErrorPlaceOCOOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetDealBlotterDataSet method
            * override this method for handling normal response from GetDealBlotterDataSet operation
            */
           public void receiveResultGetDealBlotterDataSet(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetDealBlotterDataSetResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetDealBlotterDataSet operation
           */
            public void receiveErrorGetDealBlotterDataSet(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetMarginBlotter method
            * override this method for handling normal response from GetMarginBlotter operation
            */
           public void receiveResultGetMarginBlotter(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetMarginBlotterResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetMarginBlotter operation
           */
            public void receiveErrorGetMarginBlotter(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetDealBlotterDataSetWithFilter method
            * override this method for handling normal response from GetDealBlotterDataSetWithFilter operation
            */
           public void receiveResultGetDealBlotterDataSetWithFilter(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetDealBlotterDataSetWithFilterResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetDealBlotterDataSetWithFilter operation
           */
            public void receiveErrorGetDealBlotterDataSetWithFilter(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetAccountServices method
            * override this method for handling normal response from GetAccountServices operation
            */
           public void receiveResultGetAccountServices(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetAccountServicesResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetAccountServices operation
           */
            public void receiveErrorGetAccountServices(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ModifyIfThenOrder method
            * override this method for handling normal response from ModifyIfThenOrder operation
            */
           public void receiveResultModifyIfThenOrder(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.ModifyIfThenOrderResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ModifyIfThenOrder operation
           */
            public void receiveErrorModifyIfThenOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetAccountObject method
            * override this method for handling normal response from GetAccountObject operation
            */
           public void receiveResultGetAccountObject(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetAccountObjectResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetAccountObject operation
           */
            public void receiveErrorGetAccountObject(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetDealBlotterWithFilter method
            * override this method for handling normal response from GetDealBlotterWithFilter operation
            */
           public void receiveResultGetDealBlotterWithFilter(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetDealBlotterWithFilterResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetDealBlotterWithFilter operation
           */
            public void receiveErrorGetDealBlotterWithFilter(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetOrderBlotter method
            * override this method for handling normal response from GetOrderBlotter operation
            */
           public void receiveResultGetOrderBlotter(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetOrderBlotterResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetOrderBlotter operation
           */
            public void receiveErrorGetOrderBlotter(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for SaveUserProductSubscriptionSettings method
            * override this method for handling normal response from SaveUserProductSubscriptionSettings operation
            */
           public void receiveResultSaveUserProductSubscriptionSettings(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.SaveUserProductSubscriptionSettingsResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from SaveUserProductSubscriptionSettings operation
           */
            public void receiveErrorSaveUserProductSubscriptionSettings(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for DealRequestAtBest method
            * override this method for handling normal response from DealRequestAtBest operation
            */
           public void receiveResultDealRequestAtBest(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.DealRequestAtBestResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from DealRequestAtBest operation
           */
            public void receiveErrorDealRequestAtBest(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetPositionBlotter method
            * override this method for handling normal response from GetPositionBlotter operation
            */
           public void receiveResultGetPositionBlotter(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetPositionBlotterResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetPositionBlotter operation
           */
            public void receiveErrorGetPositionBlotter(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetAccount method
            * override this method for handling normal response from GetAccount operation
            */
           public void receiveResultGetAccount(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetAccountResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetAccount operation
           */
            public void receiveErrorGetAccount(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for ModifyOCOOrder method
            * override this method for handling normal response from ModifyOCOOrder operation
            */
           public void receiveResultModifyOCOOrder(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.ModifyOCOOrderResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from ModifyOCOOrder operation
           */
            public void receiveErrorModifyOCOOrder(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetSymbolBlotter method
            * override this method for handling normal response from GetSymbolBlotter operation
            */
           public void receiveResultGetSymbolBlotter(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetSymbolBlotterResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetSymbolBlotter operation
           */
            public void receiveErrorGetSymbolBlotter(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for GetTicker method
            * override this method for handling normal response from GetTicker operation
            */
           public void receiveResultGetTicker(
                    com.cleartraders.autotrader.gainwsapi.ServiceStub.GetTickerResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from GetTicker operation
           */
            public void receiveErrorGetTicker(java.lang.Exception e) {
            }
                


    }
    