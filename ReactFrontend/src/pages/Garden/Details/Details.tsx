import { SectionBoxStyled } from "components/SectionBox.styled";
import {DetailsStyled} from "./Details.styled";
// import React from "react";

// type Props = {};

// export default function Details({}: Props) {
//   return <SectionBoxStyled>Detail</SectionBoxStyled>;
// }

import * as React from 'react';
import Tabs from '@mui/material/Tabs';
import Tab from '@mui/material/Tab';
import Typography from '@mui/material/Typography';
import Box from '@mui/material/Box';

interface TabPanelProps {
  children?: React.ReactNode;
  index: number;
  value: number;
}

function TabPanel(props: TabPanelProps) {
  const { children, value, index, ...other } = props;

  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`simple-tabpanel-${index}`}
      aria-labelledby={`simple-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box sx={{ p: 3 }}>
          <Typography>{children}</Typography>
        </Box>
      )}
    </div>
  );
}

function a11yProps(index: number) {
  return {
    id: `simple-tab-${index}`,
    'aria-controls': `simple-tabpanel-${index}`,
  };
}

export default function BasicTabs() {
  const [value, setValue] = React.useState(0);

  const handleChange = (event: React.SyntheticEvent, newValue: number) => {
    setValue(newValue);
  };

  return (
    <SectionBoxStyled>
      <DetailsStyled>
        <Box sx={{ width: '100%' }}>
          <Box sx={{ borderBottom: 1, borderColor: 'divider' }}>
            <Tabs value={value} onChange={handleChange} aria-label="basic tabs example">
              <Tab label="Garden" {...a11yProps(0)} />
              <Tab label="Ballot" {...a11yProps(1)} />
              <Tab label="Directions" {...a11yProps(2)} />
            </Tabs>
          </Box>
          <TabPanel value={value} index={0}>
            <ul>
              <li>
                What is an allotment garden?
                <p>
                  Allotment gardens are areas located within parks and gardens that house gardening plots available for lease to the community to grow their own plants. With most of the population living in high-rise flats with minimal space for gardening within their household footprint, these allotment garden plots provide them with further opportunities to garden. To date, more than 2000 allotment garden plots have been made available to the community with plans for further expansion. In tandem, NParks is working with Town Councils and Grassroot Organisations to provide advice on setting up of allotment gardens in public housing estates so that residents can garden closer to their homes. 
                </p>
              </li>

              <li>
                How should I maintain an allotment garden plot?
                <ul>
                  <p></p>
                  <li>regularly pruning plants to keep the plot tidy</li>
                  <li>removing weeds, dead and diseased plants</li>
                  <li>clearing leaf litter and any debris from their plot and surrounding area</li>
                  <li>storing all gardening tools away properly</li>
                  <li>maintaining shade and trellis structures and nettings in good order</li>
                  <li>checking for water ponding to prevent mosquito breeding</li>
                </ul>
                <p>Nparks reserves the right to terminate the lease of the allotment plot and reallocate should the allotment gardener not maintain the upkeep of their plot. </p>
              </li>

              <li>
                What are some recommended plants I can grow?
                  <p>Here is a list of suggested edible plants you can grow:</p>
                  <p>Bayam, Cai Xin, Kang Kong, Kailan, Kale, Lettuce, Okinawa Spinach, Sweet Potato, Ulam Raja, Brinjal, Chilli, Lady’s-Finger, Long Bean, Tomato, Basil, Cekur, Chives, Cincau, Indian Borage, Laska, Mint, Oyster Plant, Pandan, Saw Tooth Coriander, Tumeric</p>
                  <p>All plants and gardening structures should not exceed 1 metre in standing height
                    (measured from soil level in the planter bed). Gardeners are advised not to plant
                    poisonous plants or those with sap as these plants can cause discomfort and are harmful
                    to the public. Plants that are illegal are not allowed to be planted.
                    Please also be mindful of the neighbouring plots when planting up your mini garden and
                    ensure that your plants do not encroach into their space. Keep your space clean and tidy
                    for the safety of all your fellow allotment gardeners.
                  </p>
              </li>

              <li>
                What is the lease duration of an allotment garden plot? 
                <p>The allotment garden plot is leased on a 3-year basis. You will need to pay for 3 years at the start of your leasing period.</p>
              </li>
            </ul>

            <p>Have more questions about the Allotment Gardens Scheme? 
              Please refer to the
              <a href="https://www.nparks.gov.sg/-/media/nparks-real-content/gardening/allotment-gardening/allotment-gardens-faqs_mar-2022.ashx?la=en&hash=2955B790F8BA94458309D0E575D130EEB6BEA661&hash=2955B790F8BA94458309D0E575D130EEB6BEA661" target="blank"> FAQs </a>
              here. 
            </p>
            

          </TabPanel>
          <TabPanel value={value} index={1}>
            <ul>
            <li>
              How to place a ballot?
              <p>
                NParks will continue to roll out new allotment garden plots in more parks across 
                Singapore to provide the community with opportunities to garden near their homes. 
                To date, more than 2000 allotment garden plots have been introduced in 23 parks and 
                gardens across Singapore. We will also continue to provide more allotment gardens in 
                the future.
              </p>
              <p>
                To place a ballot, simply click on the “Place bid” tab found on the details page 
                of the garden you would like to ballot for. You will be notified about the outcome 
                of your ballot via email. 
              </p>
            </li>

            <li>
              What are the restrictions for balloting?
              <p>Each household is allowed to apply for and lease only 1 allotment garden plot at 
                any one time, regardless of the number of persons in the household. If there are 
                multiple applications received from the same household address, only 1 entry will 
                be considered for balloting. 
              </p>
            </li>

            <li>
              How much does it cost to lease an allotment plot?
              <p>
                The lease period for these plots is up to 3 years at a charge of 
                $57 per year (excluding GST).
              </p>
            </li>

            <li>
              If I live close to the new allotment garden plot, do I stand a higher chance to secure a plot?
              <p>
                Singaporeans and Permanent Residents are eligible to register for an allotment garden plot when 
                registration exercises open on NParks website. Each household is allowed to apply for and lease only 
                1 allotment garden plot at any one time across the island. The minimum age to apply for a plot is 
                18 years old at the time of application. New allotment garden plots are allocated by computerised 
                balloting. Priority is accorded to applicants by virtue of proximity of the allotment garden to the 
                applicant’s address. This ensures fair opportunity for all applicants to secure a plot. 
              </p>
            </li>

            <li>
              Is there a waiting list for the balloting system?
              <p>
              We do not maintain a waiting list. If you are interested in an allotment plot in a particular 
              park, please check our webpage regularly and submit an application when registration opens for 
              that site.
              </p>
            </li>
            </ul>
          </TabPanel>
          <TabPanel value={value} index={2}>
            Item Three
          </TabPanel>
        </Box>
      </DetailsStyled>
    </SectionBoxStyled>
  );
}
