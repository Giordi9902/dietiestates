import { Routes } from '@angular/router';
import { Hero } from './pages/hero/hero';
import { Privacy } from './pages/info-pages/privacy/privacy';
import { About } from './pages/info-pages/about/about';
import { Login } from './pages/authentication-pages/login/login';
import { Register } from './pages/authentication-pages/register/register';
import { Activate } from './pages/authentication-pages/activate/activate';
import { guestGuard } from './services/guards/guest-guard';
import { Profile } from './pages/profile/profile';
import { authGuard } from './services/guards/auth-guard';
import { Search } from './pages/search/search';
import { DashboardLayout } from './pages/dashboard/dashboard-layout/dashboard-layout';
import { AgencyHomepage } from './pages/dashboard/agency-homepage/agency-homepage';
import { roleGuard } from './services/guards/role-guard';
import { AgentsManagement } from './pages/dashboard/agents-management/agents-management';
import { CreateNewAgent } from './pages/dashboard/agents-management/create-new-agent/create-new-agent';
import { ListingsManagement } from './pages/dashboard/listings-management/listings-management';
import { CreateNewListing } from './pages/dashboard/listings-management/create-new-listing/create-new-listing';
import { Forbidden } from './pages/forbidden/forbidden';
import { NotFound } from './pages/not-found/not-found';
import { Listing } from './pages/listing/listing';
import { UpdateListing } from './pages/dashboard/listings-management/update-listing/update-listing';
import { Oauth2Success } from './pages/oauth2-success/oauth2-success';

export const routes: Routes = [
  {
    path: '',
    component: Hero,
    title: 'DietiEstates25-Home',
  },
  {
    path: 'listings/:id',
    component: Listing,
    title: 'Listing details',
  },
  {
    path: 'privacy',
    component: Privacy,
    title: 'Privacy',
  },
  {
    path: 'about',
    component: About,
    title: 'About Us',
  },
  {
    path: 'forbidden',
    component: Forbidden,
    title: 'Access Denied',
  },
  {
    path: 'login',
    component: Login,
    canActivate: [guestGuard],
    title: 'Sign in',
  },
  {
    path: 'register',
    component: Register,
    canActivate: [guestGuard],
    title: 'Register',
  },
  {
    path: 'activate',
    component: Activate,
    canActivate: [guestGuard],
    title: 'Activate account',
  },
  {
    path: 'profile',
    component: Profile,
    title: 'Profile',
    canActivate: [authGuard],
  },
  {
    path: 'search',
    component: Search,
    title: 'Search listings',
  },
  {
    path: 'oauth2-success',
    component: Oauth2Success,
  },
  {
    path: 'dashboard',
    component: DashboardLayout,
    canActivate: [authGuard],
    canMatch: [roleGuard(['ROLE_ADMIN', 'ROLE_AGENT'])],
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'homepage' },
      { path: 'homepage', component: AgencyHomepage, title: 'Dashboard' },
      {
        path: 'agents',
        component: AgentsManagement,
        canMatch: [roleGuard(['ROLE_ADMIN', 'ROLE_ADMIN_ASSISTANT'])],
        title: 'Manage your agents',
      },

      {
        path: 'agents-new',
        component: CreateNewAgent,
        canMatch: [roleGuard(['ROLE_ADMIN', 'ROLE_ADMIN_ASSISTANT'])],
        title: 'Create a new agent profile',
      },
      {
        path: 'listings',
        component: ListingsManagement,
        canMatch: [roleGuard(['ROLE_ADMIN', 'ROLE_AGENT'])],
        title: 'Manage your listings',
      },
      {
        path: 'listings-new',
        component: CreateNewListing,
        title: 'Add a new listing',
        canMatch: [roleGuard(['ROLE_ADMIN', 'ROLE_AGENT'])],
      },
      {
        path: 'listings/:id/edit',
        component: UpdateListing,
        title: 'Edit listing',
        canMatch: [roleGuard(['ROLE_ADMIN', 'ROLE_AGENT'])],
      },
    ],
  },
  { path: '**', component: NotFound, title: 'Page Not Found' },
];
