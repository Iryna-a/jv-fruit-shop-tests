package core.basesyntax.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import core.basesyntax.db.Storage;
import core.basesyntax.model.FruitTransaction;
import core.basesyntax.service.FruitService;
import core.basesyntax.strategy.OperationHandler;
import core.basesyntax.strategy.OperationStrategy;
import core.basesyntax.strategy.impl.BalanceOperationStrategy;
import core.basesyntax.strategy.impl.OperationStrategyImpl;
import core.basesyntax.strategy.impl.PurchaseOperationStrategy;
import core.basesyntax.strategy.impl.ReturnOperationStrategy;
import core.basesyntax.strategy.impl.SupplyOperationStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FruitServiceImplTest {
    private static FruitService fruitService;
    private static OperationStrategy operationStrategy;
    private static int oldStorageSize;
    private static int newStorageSize;
    private static List<FruitTransaction> fruitTransactionList;

    @BeforeClass
    public static void beforeClass() {
        Map<FruitTransaction.Operation, OperationHandler> operationStrategyMap = new HashMap<>();
        operationStrategyMap.put(FruitTransaction.Operation.BALANCE,
                new BalanceOperationStrategy());
        operationStrategyMap.put(FruitTransaction.Operation.PURCHASE,
                new PurchaseOperationStrategy());
        operationStrategyMap.put(FruitTransaction.Operation.RETURN,
                new ReturnOperationStrategy());
        operationStrategyMap.put(FruitTransaction.Operation.SUPPLY,
                new SupplyOperationStrategy());
        operationStrategy = new OperationStrategyImpl(operationStrategyMap);
        fruitService = new FruitServiceImpl(operationStrategy);
    }

    @Before
    public void setUp() {
        fruitTransactionList = new ArrayList<>();
    }

    @Test
    public void processFruitTransactions_validList_ok() {
        fruitTransactionList.add(new FruitTransaction(FruitTransaction.Operation.BALANCE,
                "banana", 20));
        fruitTransactionList.add(new FruitTransaction(FruitTransaction.Operation.PURCHASE,
                "banana", 10));
        fruitTransactionList.add(new FruitTransaction(FruitTransaction.Operation.RETURN,
                "banana", 5));
        fruitTransactionList.add(new FruitTransaction(FruitTransaction.Operation.SUPPLY,
                "banana", 100));
        fruitService.processFruitTransactions(fruitTransactionList);
        newStorageSize = Storage.fruits.size();
        assertNotEquals(oldStorageSize, newStorageSize);
    }

    @Test
    public void processFruitTransactions_emptyList_notOk() {
        fruitService.processFruitTransactions(fruitTransactionList);
        newStorageSize = Storage.fruits.size();
        assertEquals(oldStorageSize, newStorageSize);
    }

    @After
    public void tearDown() {
        Storage.fruits.clear();
    }
}