package com.ddominguezh.cqrs.client.shared.infrastructure.bus.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.ddominguezh.cqrs.shared.domain.bus.command.Command;
import com.ddominguezh.cqrs.shared.domain.bus.command.CommandBus;
import com.ddominguezh.cqrs.shared.domain.bus.command.CommandHandler;
import com.ddominguezh.cqrs.shared.domain.bus.command.CommandHandlerExecutionError;
import com.ddominguezh.cqrs.shared.infrastructure.bus.command.CommandHandlersInformation;

@Service
public final class InMemoryCommandBus implements CommandBus {
	
    private final CommandHandlersInformation information;
    private final ApplicationContext context;

    @Autowired
    public InMemoryCommandBus(ApplicationContext context) {
        this.information = new CommandHandlersInformation();
        this.context = context;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void dispatch(Command command) throws CommandHandlerExecutionError {
        try {
            Class<? extends CommandHandler> commandHandlerClass = information.search(command.getClass());
            CommandHandler handler = context.getBean(commandHandlerClass);
            handler.handle(command);
        } catch (Throwable error) {
            throw new CommandHandlerExecutionError(error);
        }
    }
}
