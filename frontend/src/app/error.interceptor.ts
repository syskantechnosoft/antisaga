import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

export const ErrorInterceptor: HttpInterceptorFn = (req, next) => {
    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {
            let errorMessage = 'An unknown error occurred!';

            if (error.error instanceof ErrorEvent) {
                // Client-side error
                errorMessage = `Error: ${error.error.message}`;
            } else {
                // Server-side error
                if (error.status === 0) {
                    errorMessage = 'Cannot connect to Server. Please check if backend is running.';
                } else {
                    errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
                    if (error.error && error.error.message) {
                        errorMessage = error.error.message;
                    }
                }
            }

            // Use a simplistic alert for now, or a Global Service
            alert(errorMessage);
            return throwError(() => error);
        })
    );
};
