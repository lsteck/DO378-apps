package org.acme.rest.json;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;


@Path("/expenses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ExpenseResource {

    // private BigDecimal maxSingleExpenseAmount = new BigDecimal(5000);
    // private BigDecimal maxMonthlyExpenseAmount = new BigDecimal(50000);

    // @ConfigProperty(name = "max-single-expense-amount")    
    // private BigDecimal maxSingleExpenseAmount;
    // @ConfigProperty(name = "max-monthly-expense-amount")
    // private BigDecimal maxMonthlyExpenseAmount;

    // @ConfigProperty(name="single-limit-error-msg", 
    //     defaultValue = "Expense amount is larger than the limit (%s)")
    // private String singleLimitErrorMsg;
    // @ConfigProperty(name = "monthly-limit-error-msg", 
    //     defaultValue = "If expense is accepted, it would exceed the monthly expense limit (%s)")
    // private String monthlyLimitErrorMsg;

    @Inject
    ExpenseConfiguration configuration;

    @GET
    public List<Expense> list() {
        return Expense.listAll();
    }

    @POST
    @Transactional
    public Expense create(final Expense expense) {
        if (expense.amount.compareTo(configuration.getMaxSingleExpenseAmount()) > 0) {
            throw new WebApplicationException(
                String.format(configuration.getSingleLimitErrorMsg(), configuration.getMaxSingleExpenseAmount()),
                Response.Status.NOT_ACCEPTABLE);
        }
	TemporalAdjuster temporalAdjuster = TemporalAdjusters.lastDayOfMonth();
        var beginDate = LocalDateTime.now().withDayOfMonth(1);
	var endDate = LocalDateTime.now().with(temporalAdjuster);

        var expensesFromMonth = Expense.listFromRange(beginDate, endDate);
        BigDecimal currentTotalAmount = expensesFromMonth.stream()
            .map(currentExpense -> currentExpense.amount)
            .reduce(new BigDecimal(0), (a, b) -> a.add(b));
        if (currentTotalAmount.add(expense.amount).compareTo(configuration.getMaxMonthlyExpenseAmount()) > 0) {
            throw new WebApplicationException(
                String.format(configuration.getMonthlyLimitErrorMsg(), configuration.getMaxMonthlyExpenseAmount()),
                Response.Status.NOT_ACCEPTABLE);
        }
        Expense newExpense = Expense.of(expense.name, expense.paymentMethod, expense.amount.toString());
        newExpense.persist();

        return newExpense;
    }

    @DELETE
    @Path("{uuid}")
    @Transactional
    public List<Expense> delete(@PathParam("uuid") final UUID uuid) {
        long numExpensesDeleted = Expense.delete("uuid", uuid);

        if (numExpensesDeleted == 0) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return Expense.listAll();
    }

    @PUT
    @Transactional
    public void update(final Expense expense) {
        Expense.update(expense);
    }
}
