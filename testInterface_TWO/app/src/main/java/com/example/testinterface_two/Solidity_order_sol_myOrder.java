package com.example.testinterface_two;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.BaseEventResponse;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 1.4.1.
 */
@SuppressWarnings("rawtypes")
public class Solidity_order_sol_myOrder extends Contract {
    public static final String BINARY = "608060405234801561001057600080fd5b506040516104d73803806104d78339818101604052810190610032919061024c565b60008060008060008060208c0151955060208b0151945060208a015193506020890151925060208801519150602087015190508c60405161007391906103f9565b60405180910390207f8a594b69b6cd8d2bee5213013be59fd1dd7617492d6a035f48dc66b096042e318787878787876040516100b496959493929190610429565b60405180910390a25050505050505050505050505061048a565b6000604051905090565b600080fd5b600080fd5b600080fd5b600080fd5b6000601f19601f8301169050919050565b7f4e487b7100000000000000000000000000000000000000000000000000000000600052604160045260246000fd5b610135826100ec565b810181811067ffffffffffffffff82111715610154576101536100fd565b5b80604052505050565b60006101676100ce565b9050610173828261012c565b919050565b600067ffffffffffffffff821115610193576101926100fd565b5b61019c826100ec565b9050602081019050919050565b60005b838110156101c75780820151818401526020810190506101ac565b838111156101d6576000848401525b50505050565b60006101ef6101ea84610178565b61015d565b90508281526020810184848401111561020b5761020a6100e7565b5b6102168482856101a9565b509392505050565b600082601f830112610233576102326100e2565b5b81516102438482602086016101dc565b91505092915050565b600080600080600080600060e0888a03121561026b5761026a6100d8565b5b600088015167ffffffffffffffff811115610289576102886100dd565b5b6102958a828b0161021e565b975050602088015167ffffffffffffffff8111156102b6576102b56100dd565b5b6102c28a828b0161021e565b965050604088015167ffffffffffffffff8111156102e3576102e26100dd565b5b6102ef8a828b0161021e565b955050606088015167ffffffffffffffff8111156103105761030f6100dd565b5b61031c8a828b0161021e565b945050608088015167ffffffffffffffff81111561033d5761033c6100dd565b5b6103498a828b0161021e565b93505060a088015167ffffffffffffffff81111561036a576103696100dd565b5b6103768a828b0161021e565b92505060c088015167ffffffffffffffff811115610397576103966100dd565b5b6103a38a828b0161021e565b91505092959891949750929550565b600081519050919050565b600081905092915050565b60006103d3826103b2565b6103dd81856103bd565b93506103ed8185602086016101a9565b80840191505092915050565b600061040582846103c8565b915081905092915050565b6000819050919050565b61042381610410565b82525050565b600060c08201905061043e600083018961041a565b61044b602083018861041a565b610458604083018761041a565b610465606083018661041a565b610472608083018561041a565b61047f60a083018461041a565b979650505050505050565b603f806104986000396000f3fe6080604052600080fdfea2646970667358221220518d3437fcc3691bd3870edd6a24f4d1270005c81a02000afd210266d3b4c6a964736f6c634300080d0033";

    public static final Event ANEVENT_EVENT = new Event("AnEvent",
            Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>(true) {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}, new TypeReference<Bytes32>() {}));
    ;

    @Deprecated
    protected Solidity_order_sol_myOrder(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Solidity_order_sol_myOrder(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Solidity_order_sol_myOrder(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Solidity_order_sol_myOrder(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public List<AnEventEventResponse> getAnEventEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(ANEVENT_EVENT, transactionReceipt);
        ArrayList<AnEventEventResponse> responses = new ArrayList<AnEventEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            AnEventEventResponse typedResponse = new AnEventEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.a = (byte[]) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.b = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.c = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.d = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
            typedResponse.e = (byte[]) eventValues.getNonIndexedValues().get(3).getValue();
            typedResponse.f = (byte[]) eventValues.getNonIndexedValues().get(4).getValue();
            typedResponse.g = (byte[]) eventValues.getNonIndexedValues().get(5).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<AnEventEventResponse> anEventEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new Function<Log, AnEventEventResponse>() {
            @Override
            public AnEventEventResponse apply(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(ANEVENT_EVENT, log);
                AnEventEventResponse typedResponse = new AnEventEventResponse();
                typedResponse.log = log;
                typedResponse.a = (byte[]) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.b = (byte[]) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.c = (byte[]) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.d = (byte[]) eventValues.getNonIndexedValues().get(2).getValue();
                typedResponse.e = (byte[]) eventValues.getNonIndexedValues().get(3).getValue();
                typedResponse.f = (byte[]) eventValues.getNonIndexedValues().get(4).getValue();
                typedResponse.g = (byte[]) eventValues.getNonIndexedValues().get(5).getValue();
                return typedResponse;
            }
        });
    }

    public Flowable<AnEventEventResponse> anEventEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(ANEVENT_EVENT));
        return anEventEventFlowable(filter);
    }

    @Deprecated
    public static Solidity_order_sol_myOrder load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Solidity_order_sol_myOrder(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Solidity_order_sol_myOrder load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Solidity_order_sol_myOrder(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Solidity_order_sol_myOrder load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Solidity_order_sol_myOrder(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Solidity_order_sol_myOrder load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Solidity_order_sol_myOrder(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Solidity_order_sol_myOrder> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, String _order_no, String _date, String _buyer_id, String _seller_id, String _com_id, String _com_amount, String _total) {
        //String _date = null,_buyer_id = null,_seller_id = null,_com_id = null,_com_amount = null,_total = null;
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_order_no),
                new Utf8String(_date),
                new Utf8String(_buyer_id),
                new Utf8String(_seller_id),
                new Utf8String(_com_id),
                new Utf8String(_com_amount),
                new Utf8String(_total)));
        return deployRemoteCall(Solidity_order_sol_myOrder.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Solidity_order_sol_myOrder> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, String _order_no, String _date, String _buyer_id, String _seller_id, String _com_id, String _com_amount, String _total) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_order_no),
                new Utf8String(_date),
                new Utf8String(_buyer_id),
                new Utf8String(_seller_id),
                new Utf8String(_com_id),
                new Utf8String(_com_amount),
                new Utf8String(_total)));
        return deployRemoteCall(Solidity_order_sol_myOrder.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Solidity_order_sol_myOrder> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _order_no, String _date, String _buyer_id, String _seller_id, String _com_id, String _com_amount, String _total) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_order_no),
                new Utf8String(_date),
                new Utf8String(_buyer_id),
                new Utf8String(_seller_id),
                new Utf8String(_com_id),
                new Utf8String(_com_amount),
                new Utf8String(_total)));
        return deployRemoteCall(Solidity_order_sol_myOrder.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Solidity_order_sol_myOrder> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _order_no, String _date, String _buyer_id, String _seller_id, String _com_id, String _com_amount, String _total) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Utf8String(_order_no),
                new Utf8String(_date),
                new Utf8String(_buyer_id),
                new Utf8String(_seller_id),
                new Utf8String(_com_id),
                new Utf8String(_com_amount),
                new Utf8String(_total)));
        return deployRemoteCall(Solidity_order_sol_myOrder.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class AnEventEventResponse extends BaseEventResponse {
        public byte[] a;

        public byte[] b;

        public byte[] c;

        public byte[] d;

        public byte[] e;

        public byte[] f;

        public byte[] g;
    }
}


