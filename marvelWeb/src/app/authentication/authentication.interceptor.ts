import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import { AuthenticationService } from './authentication.service';

@Injectable()
export class AuthenticationInterceptor implements HttpInterceptor {

  constructor(private authenticationService: AuthenticationService) {}

  intercept(request: HttpRequest<any>, next: HttpHandler) {

    if (this.authenticationService.isAuthenticated()) {

      request = request.clone({
        setHeaders: {
          Authorization: `${this.authenticationService.token}`
        }
      });

    }

    return next.handle(request);

  }

}
