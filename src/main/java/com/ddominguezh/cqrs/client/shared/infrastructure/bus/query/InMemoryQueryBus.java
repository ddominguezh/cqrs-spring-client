package com.ddominguezh.cqrs.client.shared.infrastructure.bus.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.ddominguezh.cqrs.shared.domain.bus.query.Query;
import com.ddominguezh.cqrs.shared.domain.bus.query.QueryBus;
import com.ddominguezh.cqrs.shared.domain.bus.query.QueryHandler;
import com.ddominguezh.cqrs.shared.domain.bus.query.QueryHandlerExecutionError;
import com.ddominguezh.cqrs.shared.domain.bus.query.Response;
import com.ddominguezh.cqrs.shared.infrastructure.bus.query.QueryHandlersInformation;

@Service
public final class InMemoryQueryBus implements QueryBus {
    private final QueryHandlersInformation information;
    private final ApplicationContext context;

    @Autowired
    public InMemoryQueryBus(ApplicationContext context) {
        this.information = new QueryHandlersInformation();
        this.context = context;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public Response ask(Query query) throws QueryHandlerExecutionError {
		try {
            Class<? extends QueryHandler> queryHandlerClass = information.search(query.getClass());
            QueryHandler handler = context.getBean(queryHandlerClass);
            return handler.handle(query);
        } catch (Throwable error) {
            throw new QueryHandlerExecutionError(error);
        }
	}

}
