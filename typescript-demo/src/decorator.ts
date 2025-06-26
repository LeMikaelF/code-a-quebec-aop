export function audited<
    TThis,                        // the class instance (this)
    TArgs extends any[],          // method parameter tuple
    TResult,                      // method return type
    TExpr                         // expression's return type
>(action: 'save' | 'delete', expression: (...args: TArgs) => TExpr) {
    // this is the function invoked by adding parentheses to the annotation!

    return function(originalMethod: (this: TThis, ...args: TArgs) => TResult) {
        // this is called once per annotation

        return function(this: TThis, ...args: TArgs): TResult {
            // this function replaces the original function
            console.log(`Auditiong action '${action}', auditable id = ${expression(...args)}`);

            return originalMethod.apply(this, args);
        }
    };
}

